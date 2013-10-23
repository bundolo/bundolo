package org.bundolo.client.view.content;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditTextView {

	public interface Presenter {
		void onSaveTextButtonClicked();
		void onCancelTextButtonClicked();
		ContentDTO getContentDTO();
		void setContentDTO(ContentDTO contentDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getTitle();
	String getText();
	String getDescription();
}