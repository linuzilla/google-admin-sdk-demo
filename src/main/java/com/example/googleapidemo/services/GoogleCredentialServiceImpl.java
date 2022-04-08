package com.example.googleapidemo.services;

import com.example.googleapidemo.properties.GoogleWorkspaceProperties;
import com.example.googleapidemo.utils.ReadfileUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.reports.Reports;
import com.google.api.services.drive.Drive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
@Service
public class GoogleCredentialServiceImpl implements GoogleCredentialService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleCredentialServiceImpl.class);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final HttpTransport httpTransport;
    private final GoogleCredential credential;
    private final String applicationName;
    private Directory directory;
    private Drive drive;
    private Reports reports;

    public GoogleCredentialServiceImpl(GoogleWorkspaceProperties googleWorkspaceProperties) throws GeneralSecurityException, IOException {
        this.applicationName = googleWorkspaceProperties.getApplicationName();
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential googleCredential = GoogleCredential.fromStream(ReadfileUtil.readFrom(googleWorkspaceProperties.getJsonCredential()))
                .createScoped(googleWorkspaceProperties.getScopes());

        logger.info("Service Account Id: {}", googleCredential.getServiceAccountId());

        // New style could not set "Account User", use old style instead!
        this.credential = new GoogleCredential.Builder()
                .setTransport(googleCredential.getTransport())
                .setJsonFactory(googleCredential.getJsonFactory())
                .setServiceAccountId(googleCredential.getServiceAccountId())
                .setServiceAccountUser(googleWorkspaceProperties.getAdmin())
                .setServiceAccountScopes(googleCredential.getServiceAccountScopes())
                .setServiceAccountPrivateKey(googleCredential.getServiceAccountPrivateKey())
                .build();
    }

    @Override
    public synchronized Directory getDirectoryService() {
        if (this.directory == null) {
            this.directory = new Directory.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(applicationName).build();
        }
        return this.directory;
    }

    @Override
    public synchronized Drive getDriveService() {
        if (this.drive == null) {
            this.drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(applicationName).build();
        }
        return this.drive;
    }

    @Override
    public synchronized Reports getReportsService() {
        if (this.reports == null) {
            this.reports = new Reports.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(applicationName).build();
        }
        return this.reports;
    }
}
