package org.bundolo.client.view.user;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface AddUserProfileView {

	public interface Presenter {
		void onSaveUserProfileButtonClicked();
		void onCancelUserProfileButtonClicked();
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getUsername();
	String getPassword();
	String getEmail();
	//void displayStatusPanel(String statusMessage);
	//void displayAddUserProfilePanel();
}