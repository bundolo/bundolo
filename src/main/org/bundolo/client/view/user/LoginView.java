package org.bundolo.client.view.user;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;

import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.UserProfileDTO;

public interface LoginView {

	public interface Presenter {
		void onRegisterButtonClicked();
		void onLoginButtonClicked();
		void onGeneratePasswordButtonClicked();
		void onLoginWithFacebookButtonClicked();
		void onUserProfileButtonClicked();
		void onLogoutButtonClicked();
		void onSendPasswordButtonClicked();
		void onSendPasswordCancelButtonClicked();
		UserDTO getUserDTO();
		void setUserDTO(UserDTO userDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	String getUsername();
	String getPassword();
	Boolean getRememberMe();
	String getEmail();
	Widget asWidget();
	void displayLoginPanel();
	void displayLogoutPanel(String username);
	void displayGeneratePasswordPanel();
	
} 