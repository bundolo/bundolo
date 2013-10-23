package org.bundolo.client.view.home;

import com.google.gwt.user.client.ui.Widget;

import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.UserProfileDTO;

public interface HomeView {

	public interface Presenter {
		ItemListDTO getNewTextsItemListDTO();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
} 