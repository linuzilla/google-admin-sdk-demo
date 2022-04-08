package com.example.googleapidemo.models;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public class DrivePermissionModel {
    private String id;
    private String emailAddress;
    private String role;

    public DrivePermissionModel() {
    }

    public DrivePermissionModel(String id, String emailAddress, String role) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
