package org.bundolo.client.view.user;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.presenter.UserProfilePresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.shared.model.enumeration.PageKindType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AuthorViewImpl extends Composite implements AuthorView {

	private static final Logger logger = Logger.getLogger(AuthorViewImpl.class.getName());

	@UiTemplate("AuthorView.ui.xml")
	interface AuthorViewUiBinder extends UiBinder<Widget, AuthorViewImpl> {}
	private static AuthorViewUiBinder uiBinder = GWT.create(AuthorViewUiBinder.class);
	
	@UiField
	HTMLPanel authorContainer;

	private Presenter presenter;

	public AuthorViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public void displayAuthors() {
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getAuthorsItemListDTO(), ItemListType.simplePaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void displayAuthor() {
		authorContainer.clear();
		UserProfilePresenter userProfilePresenter = (UserProfilePresenter)LocalStorage.getInstance().getPresenter(PresenterName.user.name());
		userProfilePresenter.setUserDTO(presenter.getAuthorUserDTO());		
		userProfilePresenter.go(authorContainer);
	}

}