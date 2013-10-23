package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.UserProfileDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bundoloServices/userProfileService")
public interface UserProfileService extends RemoteService {
	
	//TODO this method should not be public. users on client side should be in UserDTO object
	public UserProfileDTO findUserProfile(Long userId);
	public void saveUserProfile(UserProfileDTO userProfileDTO) throws Exception;
//	public UserDTO updateUserProfile(UserProfileDTO userProfileDTO) throws Exception;
	public UserDTO updateUserProfile(UserProfileDTO userProfileDTO) throws Exception;
	//public void saveOrUpdateUserProfile(UserProfileDTO userProfileDTO) throws Exception;
	public void deleteUserProfile(Long userId) throws Exception;
	public UserDTO login(String username, String password, Boolean rememberMe) throws Exception;
	public UserDTO validateSession() throws Exception;
	public void logout() throws Exception;
	public Boolean activateUserProfileEmailAddress(String email, String nonce) throws Exception;
	public UserDTO findUserByUsername(String username);
	public List<UserDTO> findItemListUsers(String query, Integer start, Integer end) throws Exception;
	public Integer findItemListUsersCount(String query) throws Exception;
	public Boolean sendMessage(String title, String text, String recipientUsername) throws Exception;
}