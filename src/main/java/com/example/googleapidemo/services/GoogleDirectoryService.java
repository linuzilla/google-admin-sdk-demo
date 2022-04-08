package com.example.googleapidemo.services;

import com.google.api.services.admin.directory.model.User;

import java.io.IOException;
import java.util.List;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public interface GoogleDirectoryService {
	List<User> findAll() throws IOException;

	User findUser(String userKey) throws IOException;

	void addUser(User newUser) throws IOException;

	void addUser(String primaryEmail, String password,
                                 String familyName, String givenName) throws IOException;

	void deleteUser(User user) throws IOException;

	void deleteUser(String userEmail) throws IOException;

	User updateUserPassword(String primaryEmail, String newPassword)
			throws IOException;

	User suspendUser(String userEmail, String reason) throws IOException;

	User unsuspendUser(String userEmail) throws IOException;

	User assignOrganizationUnit(String userEmail, String organizationalUnit) throws IOException;
}