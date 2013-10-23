package org.bundolo.client.view.user;

import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.UserDTO;

import com.google.gwt.user.client.ui.Widget;

public interface UserProfileView {

	public interface Presenter {
		void onDeleteUserProfileButtonClicked();
		void onEditUserProfileButtonClicked();
		void onAddMessageButtonClicked();
		UserDTO getUserDTO();
		void setUserDTO(UserDTO userDTO);
		ContentPresenter getDescriptionContentPresenter();
		ItemListPresenter getUserTextsListPresenter();
		ItemListDTO getUserTextsItemListDTO();
	}

	void setPresenter(Presenter presenter);
	void displayUserProfile();
	void displayUserTexts();
	void refreshControlsVisibility();
	Widget asWidget();
} 