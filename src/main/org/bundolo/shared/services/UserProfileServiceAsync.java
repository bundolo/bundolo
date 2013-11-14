package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.UserProfileDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserProfileServiceAsync {

    void deleteUserProfile(Long userId, AsyncCallback<Void> callback);

    void findUserProfile(Long userId, AsyncCallback<UserProfileDTO> callback);

    void saveUserProfile(UserProfileDTO userProfileDTO, AsyncCallback<Void> callback);

    // void saveOrUpdateUserProfile(UserProfileDTO userProfileDTO,
    // AsyncCallback<Void> callback);

    // void updateUserProfile(UserProfileDTO userProfileDTO,
    // AsyncCallback<UserDTO> callback);

    void updateUserProfile(UserProfileDTO userProfileDTO, AsyncCallback<UserDTO> callback);

    void login(String username, String password, Boolean rememberMe, AsyncCallback<UserDTO> callback);

    void validateSession(AsyncCallback<UserDTO> callback);

    void logout(AsyncCallback<Void> callback);

    void activateUserProfileEmailAddress(String email, String nonce, AsyncCallback<Boolean> callback);

    void findUserByUsername(String username, AsyncCallback<UserDTO> callback);

    void findItemListUsers(String query, Integer start, Integer end, AsyncCallback<List<UserDTO>> callback);

    void findItemListUsersCount(String query, AsyncCallback<Integer> callback);

    void sendMessage(String title, String text, String recipientUsername, AsyncCallback<Boolean> callback);

    void sendNewPassword(String email, AsyncCallback<Boolean> callback);

}
