package com.example.googleapidemo.services;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.reports.Reports;
import com.google.api.services.drive.Drive;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public interface GoogleCredentialService {
	Directory getDirectoryService();
    Drive getDriveService();
    Reports getReportsService();
}
