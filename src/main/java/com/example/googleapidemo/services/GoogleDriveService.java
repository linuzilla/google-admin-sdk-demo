package com.example.googleapidemo.services;

import com.example.googleapidemo.models.PermissionEnum;
import com.example.googleapidemo.models.TeamDriveModel;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public interface GoogleDriveService {
    Collection<TeamDriveModel> getAllTeamDrives();

    TeamDriveModel findById(String driveId) throws IOException;

    void removePermissionFromTeamDrive(String driveId, String permissionId) throws IOException;

    void addPermissionToTeamDrive(String driveId, String emailAddress, PermissionEnum role) throws IOException;
}
