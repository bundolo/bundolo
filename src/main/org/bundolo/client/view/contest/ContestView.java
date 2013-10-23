package org.bundolo.client.view.contest;

import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface ContestView {

	public interface Presenter {
		void onAddContestButtonClicked();
		void onDeleteContestButtonClicked();
		void onEditContestButtonClicked();
		ItemListDTO getContestsItemListDTO();
		void setContestDTO(ContestDTO contestDTO);
		ContestDTO getContestDTO();
		ContentPresenter getContestContentPresenter();
		void reset(int level);
	}

	void setPresenter(Presenter presenter);
	void displayContests();
	void displayContest();
	void refreshControlsVisibility();
	void reset(int level);
	Widget asWidget();
} 