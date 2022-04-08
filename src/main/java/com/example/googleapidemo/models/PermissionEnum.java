package com.example.googleapidemo.models;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public enum PermissionEnum {
    ORGANIZER,
    FILE_ORGANIZER("fileOrganizer"),
    WRITER,
    READER,
    COMMENTER;

    private final String value;

    PermissionEnum() {
        this.value = name().toLowerCase();
    }

    PermissionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
