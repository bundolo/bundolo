package org.bundolo.client.view.user;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface ActivateUserProfileView {

	public interface Presenter {
		void onActivateUserProfileButtonClicked();
		void onCancelActivateUserProfileButtonClicked();
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getActivationEmail();
	String getActivationCode();
	//void displayStatusPanel(String statusMessage);
	//void displayActivationPanel();
}