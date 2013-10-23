package org.bundolo.client.view.content;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditCommentView {

	public interface Presenter {
		void onSaveCommentButtonClicked();
		void onCancelCommentButtonClicked();
		ContentDTO getContentDTO();
		void setContentDTO(ContentDTO contentDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	//String getName();
	String getText();
	//ContentKindType getKind();
	//ContentStatusType getContentStatus();
}