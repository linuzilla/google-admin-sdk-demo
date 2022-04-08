package com.example.googleapidemo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
@ConfigurationProperties("google")
public class GoogleWorkspaceProperties {
    private String applicationName;
    private String domain;
    private String admin;
    private String jsonCredential;
    private List<String> scopes;
    private String defaultOrganizationalUnit;
    private boolean enableWriting;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getJsonCredential() {
        return jsonCredential;
    }

    public void setJsonCredential(String jsonCredential) {
        this.jsonCredential = jsonCredential;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getDefaultOrganizationalUnit() {
        return defaultOrganizationalUnit;
    }

    public void setDefaultOrganizationalUnit(String defaultOrganizationalUnit) {
        this.defaultOrganizationalUnit = defaultOrganizationalUnit;
    }

    public boolean isEnableWriting() {
        return enableWriting;
    }

    public void setEnableWriting(boolean enableWriting) {
        this.enableWriting = enableWriting;
    }
}
