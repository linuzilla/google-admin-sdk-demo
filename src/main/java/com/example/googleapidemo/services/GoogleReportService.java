package com.example.googleapidemo.services;

import com.example.googleapidemo.models.DriveUsage;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.reports.model.Activity;

import java.io.IOException;
import java.util.List;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public interface GoogleReportService {
	List<User> getAccounts() throws IOException;
	List<Activity> getLoginActivities() throws IOException;
	List<DriveUsage> getDriveUsage(String theDay) throws IOException;
}
