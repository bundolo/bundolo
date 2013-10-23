package org.bundolo.client.view.user;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditMessageView {

	public interface Presenter {
		void onSendMessageButtonClicked();
		void onCancelMessageButtonClicked();
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getTitle();
	String getText();
}