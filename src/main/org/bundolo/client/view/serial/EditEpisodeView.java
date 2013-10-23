package org.bundolo.client.view.serial;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditEpisodeView {

	public interface Presenter {
		void onSaveEpisodeButtonClicked();
		void onCancelEpisodeButtonClicked();
		ContentDTO getContentDTO();
		void setContentDTO(ContentDTO contentDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getName();
	String getText();
}