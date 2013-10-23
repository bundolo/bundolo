package org.bundolo.client.view.home;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.shared.model.enumeration.PageKindType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class HomeViewImpl extends Composite implements HomeView {

	private static final Logger logger = Logger.getLogger(HomeViewImpl.class.getName());

	@UiTemplate("HomeView.ui.xml")
	interface HomeViewUiBinder extends UiBinder<Widget, HomeViewImpl> {}
	private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	private Presenter presenter;

	public HomeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public Widget asWidget() {
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getNewTextsItemListDTO(), ItemListType.simplePaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
		return this;
	}

}