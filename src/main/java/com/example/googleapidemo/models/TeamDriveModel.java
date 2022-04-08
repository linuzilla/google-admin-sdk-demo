package com.example.googleapidemo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public class TeamDriveModel {
    private String name;
    private String id;
    private List<DrivePermissionModel> permissionModels = new ArrayList<>();

    public TeamDriveModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public TeamDriveModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addPermission(DrivePermissionModel permissionModel) {
        this.permissionModels.add(permissionModel);
    }

    public List<DrivePermissionModel> getPermissionModels() {
        return permissionModels;
    }
}
