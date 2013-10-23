package org.bundolo.client.view.user;

import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.UserDTO;

import com.google.gwt.user.client.ui.Widget;

public interface AuthorView {

	public interface Presenter {
		ItemListDTO getAuthorsItemListDTO();
		void setAuthorUserDTO(UserDTO userDTO);
		UserDTO getAuthorUserDTO();
	}

	void setPresenter(Presenter presenter);
	void displayAuthors();
	void displayAuthor();
	Widget asWidget();
} 