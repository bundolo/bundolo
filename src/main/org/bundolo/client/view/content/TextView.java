package org.bundolo.client.view.content;

import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface TextView {

	public interface Presenter {
		ItemListDTO getTextsItemListDTO();
		void setTextContentDTO(ContentDTO contentDTO);
		ContentDTO getTextContentDTO();
		ContentPresenter getTextContentPresenter();
		void onAddTextButtonClicked();
		void onDeleteTextButtonClicked();
		void onEditTextButtonClicked();
	}

	void setPresenter(Presenter presenter);
	void displayTexts();
	void displayText();
	void refreshControlsVisibility();
	Widget asWidget();
} 