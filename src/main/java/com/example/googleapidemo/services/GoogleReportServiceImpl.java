package com.example.googleapidemo.services;

import com.example.googleapidemo.models.DriveUsage;
import com.example.googleapidemo.properties.GoogleWorkspaceProperties;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.reports.Reports;
import com.google.api.services.admin.reports.Reports.UserUsageReport.Get;
import com.google.api.services.admin.reports.model.Activities;
import com.google.api.services.admin.reports.model.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
@Service
public class GoogleReportServiceImpl implements GoogleReportService {
    private static final Logger logger = LoggerFactory.getLogger(GoogleReportServiceImpl.class);

    private final GoogleCredentialService googleCredentialService;
    private final GoogleDirectoryService googleDirectoryService;

    public GoogleReportServiceImpl(GoogleCredentialService googleCredentialService, GoogleDirectoryService googleDirectoryService, GoogleWorkspaceProperties googleWorkspaceProperties) {
        this.googleCredentialService = googleCredentialService;
        this.googleDirectoryService = googleDirectoryService;
    }

    @Override
    public List<User> getAccounts() throws IOException {
        return googleDirectoryService.findAll();
    }

    private static String formatDateToRfc3339(Date date) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat rfc3339 = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        rfc3339.setTimeZone(utc);
        return rfc3339.format(date);
    }

    @Override
    public List<Activity> getLoginActivities() throws IOException {
        final var reportsService = googleCredentialService.getReportsService();

        final List<Activity> allLogins = new ArrayList<>();
        com.google.api.services.admin.reports.Reports.Activities activities = reportsService.activities();

        long SEVEN_DAYS_IN_MS = 1000 * 60 * 60 * 24 * 7;
        String startTime = formatDateToRfc3339(new Date(
                System.currentTimeMillis() - SEVEN_DAYS_IN_MS));

        Reports.Activities.List request = activities.list("all", "login")
                .setStartTime(startTime);
        do {
            try {
                Activities currentPage = request.execute();
                allLogins.addAll(currentPage.getItems());
                request.setPageToken(currentPage.getNextPageToken());
            } catch (IOException e) {
                logger.warn("An error occurred: {}", e.getMessage());
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);

        return allLogins;
    }

    @Override
    public List<DriveUsage> getDriveUsage(String theDay) throws IOException {
        final var reportsService = googleCredentialService.getReportsService();
        final var userUsageReport = reportsService.userUsageReport();

        // email,accounts:drive_used_quota_in_mb
        Get request = userUsageReport.get("all", theDay);
        request.setParameters("accounts:used_quota_in_mb,accounts:last_login_time");

        final List<DriveUsage> allLogins = new ArrayList<>();

        do {
            try {
                Optional.ofNullable(request.execute())
                        .ifPresent(currentPage -> {
                            currentPage.getUsageReports().stream()
                                    .map(usageReport -> new DriveUsage(
                                            usageReport.getEntity().getUserEmail().replaceAll("@.*", ""),
                                            du -> usageReport.getParameters().forEach(parameter -> {
                                                if ("accounts:used_quota_in_mb".equals(parameter.getName())) {
                                                    du.setMb(parameter.getIntValue());
                                                } else if ("accounts:last_login_time".equals(parameter.getName())) {
                                                    du.setLastUse(parameter.getDatetimeValue());
                                                }
                                            })))
                                    .forEach(allLogins::add);
                            request.setPageToken(currentPage.getNextPageToken());
                        });
            } catch (IOException e) {
                logger.warn("An error occurred: {}", e.getMessage());
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);

        return allLogins;
    }
}
