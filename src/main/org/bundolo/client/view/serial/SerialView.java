package org.bundolo.client.view.serial;

import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface SerialView {

	public interface Presenter {
		ItemListDTO getSerialsItemListDTO();
		ItemListDTO getSerialEpisodesItemListDTO();
		void setSerialContentDTO(ContentDTO contentDTO);
		void setEpisodeContentDTO(ContentDTO contentDTO);
		ContentDTO getSerialContentDTO();
		ContentDTO getEpisodeContentDTO();
		ItemListPresenter getSerialEpisodeListPresenter();
		void onAddSerialButtonClicked();
		void onDeleteSerialButtonClicked();
		void onEditSerialButtonClicked();
		void onAddEpisodeButtonClicked();
		void onDeleteEpisodeButtonClicked();
		void onEditEpisodeButtonClicked();
	}

	void setPresenter(Presenter presenter);
	void displaySerialEpisodes();
	void displaySerials();
	void displayEpisode();
	void refreshControlsVisibility();
	Widget asWidget();
} 