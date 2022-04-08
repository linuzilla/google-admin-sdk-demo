package com.example.googleapidemo.services;

import com.example.googleapidemo.properties.GoogleWorkspaceProperties;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.UserName;
import com.google.api.services.admin.directory.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
@Service
public class GoogleDirectoryServiceImpl implements GoogleDirectoryService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleDirectoryServiceImpl.class);

    private final GoogleCredentialService googleCredentialService;
    private final GoogleWorkspaceProperties googleWorkspaceProperties;

    public GoogleDirectoryServiceImpl(GoogleCredentialService googleCredentialService, GoogleWorkspaceProperties googleWorkspaceProperties) {
        this.googleCredentialService = googleCredentialService;
        this.googleWorkspaceProperties = googleWorkspaceProperties;
    }

    @Override
    public List<User> findAll() throws IOException {
        List<User> allUsers = new ArrayList<>();

        Directory.Users.List request = googleCredentialService.getDirectoryService().users().list().setDomain(googleWorkspaceProperties.getDomain());

        do {
            try {
                Users currentPage = request.execute();
                allUsers.addAll(currentPage.getUsers());
                request.setPageToken(currentPage.getNextPageToken());
            } catch (IOException e) {
                logger.warn("An error occurred: {}", e.getMessage());
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);

        return allUsers;
    }

    @Override
    public User findUser(String userKey) throws IOException {
        return googleCredentialService.getDirectoryService().users().get(userKey + "@" + googleWorkspaceProperties.getDomain()).execute();
    }

    @Override
    public void addUser(User newUser) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            newUser.setChangePasswordAtNextLogin(true);
            googleCredentialService.getDirectoryService().users().insert(newUser).execute();
        }
    }

    @Override
    public void addUser(String primaryEmail, String password, String familyName, String givenName) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            UserName uname = new UserName();
            uname.setFamilyName(familyName);
            uname.setGivenName(givenName);

            User newUser = new User();
            newUser.setName(uname);
            newUser.setChangePasswordAtNextLogin(true);
            newUser.setPassword(password);
            newUser.setPrimaryEmail(primaryEmail);
            newUser.setOrgUnitPath(googleWorkspaceProperties.getDefaultOrganizationalUnit());

            googleCredentialService.getDirectoryService().users().insert(newUser).execute();
        }
    }

    @Override
    public User updateUserPassword(String primaryEmail, String newPassword) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            User user = new User();
            user.setChangePasswordAtNextLogin(true);
            user.setPassword(newPassword);
            return googleCredentialService.getDirectoryService().users().update(primaryEmail, user).execute();
        } else {
            return findUser(primaryEmail);
        }
    }

    @Override
    public void deleteUser(User user) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            googleCredentialService.getDirectoryService().users().delete(user.getPrimaryEmail()).execute();
        }
    }

    @Override
    public void deleteUser(String userEmail) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            googleCredentialService.getDirectoryService().users().delete(userEmail).execute();
        }
    }

    @Override
    public User suspendUser(String userEmail, String reason) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            User user = new User();
            user.setSuspended(true);
            user.setSuspensionReason(reason);
            return googleCredentialService.getDirectoryService().users().update(userEmail, user).execute();
        } else {
            return findUser(userEmail);
        }
    }

    @Override
    public User unsuspendUser(String userEmail) throws IOException {
        if (googleWorkspaceProperties.isEnableWriting()) {
            User user = new User();
            user.setSuspended(false);
            return googleCredentialService.getDirectoryService().users().update(userEmail, user).execute();
        } else {
            return findUser(userEmail);
        }
    }

    @Override
    public User assignOrganizationUnit(String userEmail, String organizationalUnit) throws IOException {
        final var user = new User();
        user.setOrgUnitPath(organizationalUnit);
        return googleCredentialService.getDirectoryService().users().update(userEmail, user).execute();
    }
}
