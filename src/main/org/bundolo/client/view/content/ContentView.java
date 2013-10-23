package org.bundolo.client.view.content;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface ContentView {

	public interface Presenter {
		void onAddCommentButtonClicked(ContentDTO contentDTO);
		void onDeleteCommentButtonClicked(ContentDTO contentDTO);
		void onEditCommentButtonClicked(ContentDTO contentDTO);
		ContentDTO getContentDTO();
		void setContentDTO(ContentDTO contentDTO);
//		ContentDTO getRootComment();
//		void setRootComment(ContentDTO contentDTO);
		//void setPage(PageDTO pageDTO);
	}

	void setPresenter(Presenter presenter);
	void displayContent();
	void refreshControlsVisibility();
	Widget asWidget();
} 