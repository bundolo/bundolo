package org.bundolo.client.view.news;

import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface NewsView {

	public interface Presenter {
		ItemListDTO getNewsItemListDTO();
		void setNewsContentDTO(ContentDTO contentDTO);
		ContentDTO getNewsContentDTO();
		ContentPresenter getNewsContentPresenter();
		void onAddNewsButtonClicked();
		void onDeleteNewsButtonClicked();
		void onEditNewsButtonClicked();
	}

	void setPresenter(Presenter presenter);
	void displayNews();
	void displaySingleNews();
	void refreshControlsVisibility();
	Widget asWidget();
} 