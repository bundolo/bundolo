package org.bundolo.client.view.news;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditNewsView {

	public interface Presenter {
		void onSaveNewsButtonClicked();
		void onCancelNewsButtonClicked();
		ContentDTO getContentDTO();
		void setContentDTO(ContentDTO contentDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getName();
	String getText();
}