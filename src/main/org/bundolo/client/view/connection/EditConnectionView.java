package org.bundolo.client.view.connection;

import org.bundolo.shared.model.ConnectionDTO;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditConnectionView {

	public interface Presenter {
		void onSaveConnectionButtonClicked();
		void onCancelConnectionButtonClicked();
		ConnectionDTO getConnectionDTO();
		void setConnectionDTO(ConnectionDTO connectionDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getName();
	String getDescription();
	String getEmail();
	String getUrl();
}