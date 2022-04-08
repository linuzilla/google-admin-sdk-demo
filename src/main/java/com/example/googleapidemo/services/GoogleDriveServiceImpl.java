package com.example.googleapidemo.services;

import com.example.googleapidemo.models.DrivePermissionModel;
import com.example.googleapidemo.models.PermissionEnum;
import com.example.googleapidemo.models.TeamDriveModel;
import com.example.googleapidemo.properties.GoogleWorkspaceProperties;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.api.services.drive.model.TeamDrive;
import com.google.api.services.drive.model.TeamDriveList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);

    private final GoogleWorkspaceProperties googleWorkspaceProperties;
    private final GoogleCredentialService googleCredentialService;

    public GoogleDriveServiceImpl(GoogleWorkspaceProperties googleWorkspaceProperties, GoogleCredentialService googleCredentialService) {
        this.googleWorkspaceProperties = googleWorkspaceProperties;
        this.googleCredentialService = googleCredentialService;
    }

    private void readPermissions(Map<String, TeamDriveModel> drives) {
        drives.forEach((id, teamDriveModel) -> {
            try {
                getPermission(teamDriveModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private synchronized Map<String, TeamDriveModel> findAllTeamDrives() {
        final Map<String, TeamDriveModel> allTeamDrives = new HashMap<>();

        try {
            final var googleDrive = googleCredentialService.getDriveService();

            Drive.Teamdrives.List request = googleDrive.teamdrives().list().setUseDomainAdminAccess(true);
            String pageToken = null;

            do {
                TeamDriveList result = googleDrive.teamdrives().list()
                        //                    .setQ("organizerCount = 0")
                        .setFields("nextPageToken, teamDrives(id, name)")
                        .setUseDomainAdminAccess(true)
                        .setPageToken(pageToken)
                        .execute();
                result.getTeamDrives().forEach(teamDrive -> {
                    logger.info("TeamDrive: {}, {}", teamDrive.getId(), teamDrive.getName());
                    allTeamDrives.put(teamDrive.getId(), new TeamDriveModel(teamDrive.getName(), teamDrive.getId()));
                });
                pageToken = result.getNextPageToken();
            } while (pageToken != null);

            readPermissions(allTeamDrives);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allTeamDrives;
    }


    private void getPermission(TeamDriveModel teamDriveModel) throws IOException {
        googleCredentialService.getDriveService().permissions().list(teamDriveModel.getId())
                .setSupportsTeamDrives(true)
                .setUseDomainAdminAccess(true)
                .setFields("nextPageToken, permissions(kind,id,type,emailAddress,role,deleted)").execute()
                .getPermissions()
                .forEach(permission -> teamDriveModel.addPermission(
                        new DrivePermissionModel(permission.getId(), permission.getEmailAddress(), permission.getRole())));
    }

    @Override
    public TeamDriveModel findById(String driveId) throws IOException {
        Drive googleDrive = googleCredentialService.getDriveService();

        try {
            TeamDrive teamDrive = googleDrive.teamdrives().get(driveId).setUseDomainAdminAccess(true).execute();


//            googleDrive.permissions().

            TeamDriveModel teamDriveModel = new TeamDriveModel(teamDrive.getName(), teamDrive.getId());

            Drive.Permissions.List reqeust = googleDrive.permissions().list(driveId)
                    .setSupportsTeamDrives(true)
                    .setUseDomainAdminAccess(true);

            do {
                PermissionList permissionList = reqeust
                        .setFields("nextPageToken, permissions(kind,id,type,emailAddress,role,deleted)").execute();

                for (Permission permission : permissionList.getPermissions()) {
                    DrivePermissionModel permissionModel = new DrivePermissionModel();
                    permissionModel.setId(permission.getId());
                    permissionModel.setEmailAddress(permission.getEmailAddress());
                    permissionModel.setRole(permission.getRole());
                    teamDriveModel.addPermission(permissionModel);
                }

            } while (reqeust.getPageToken() != null && reqeust.getPageToken().length() > 0);

            return teamDriveModel;
        } catch (IOException e) {
            throw e;
        }
    }


    @Override
    public Collection<TeamDriveModel> getAllTeamDrives() {
        return findAllTeamDrives().values();
    }

    @Override
    public void removePermissionFromTeamDrive(String driveId, String permissionId) throws IOException {
        Drive googleDrive = googleCredentialService.getDriveService();

        TeamDrive teamDrive = googleDrive.teamdrives().get(driveId).setUseDomainAdminAccess(true).execute();

        googleDrive.permissions()
                .delete(teamDrive.getId(), permissionId)
                .setUseDomainAdminAccess(true)
                .setSupportsTeamDrives(true)
                .execute();
    }

    @Override
    public void addPermissionToTeamDrive(String driveId, String emailAddress, PermissionEnum permissionEnum) throws IOException {
        Drive googleDrive = googleCredentialService.getDriveService();

        Permission newPermission = new Permission()
                .setType("user")
                .setRole(permissionEnum.getValue())
                .setEmailAddress(emailAddress);

        TeamDrive teamDrive = googleDrive.teamdrives().get(driveId).setUseDomainAdminAccess(true).execute();

        Permission permissionResult = googleDrive.permissions()
                .create(teamDrive.getId(), newPermission)
                .setUseDomainAdminAccess(true)
                .setSupportsTeamDrives(true)
                .setFields("id")
                .execute();

        System.out.printf("Added permission: %s\n",
                permissionResult.getId());
    }
}
