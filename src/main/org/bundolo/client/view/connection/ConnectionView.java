package org.bundolo.client.view.connection;

import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface ConnectionView {

	public interface Presenter {
		ItemListDTO getConnectionGroupsItemListDTO();
		ItemListDTO getConnectionGroupConnectionsItemListDTO();
		void setConnectionGroupContentDTO(ContentDTO contentDTO);
		void setConnectionDTO(ConnectionDTO connectionDTO);
		ConnectionDTO getConnectionDTO();
		ContentPresenter getConnectionContentPresenter();
		ItemListPresenter getConnectionGroupConnectionListPresenter();
		void onAddConnectionButtonClicked();
		void onDeleteConnectionButtonClicked();
		void onEditConnectionButtonClicked();
		void reset(int level);
	}

	void setPresenter(Presenter presenter);
	void displayConnectionGroups();
	void displayConnectionGroupConnections();
	void displayConnection();
	void refreshControlsVisibility();
	void reset(int level);
	Widget asWidget();
} 