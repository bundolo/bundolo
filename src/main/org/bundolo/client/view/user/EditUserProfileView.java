package org.bundolo.client.view.user;

import java.util.Date;

import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.UserProfileGenderType;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditUserProfileView {

	public interface Presenter {
		void onSaveUserProfileButtonClicked();
		void onCancelUserProfileButtonClicked();
		UserDTO getUserDTO();
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getUsername();
	String getNewPassword();
	String getNewEmail();
	String getFirstName();
	String getLastName();
	UserProfileGenderType getGender();
	Date getBirthDate();
	Boolean getShowPersonal();
	String getAvatarUrl();
	String getDescription();
	String getCurrentPassword();
}