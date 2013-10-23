package org.bundolo.client.view.page;

import com.google.gwt.user.client.ui.Widget;
public interface StatusView {

public interface Presenter {
		void onContinueButtonClicked();
		void setStatusMessage(String statusMessage);
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	void setStatusMessage(String statusMessage);
}