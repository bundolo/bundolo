package org.bundolo.client.view.connection;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.client.widget.ConditionalPanel;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageKindType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class ConnectionViewImpl extends Composite implements ConnectionView {

	private static final Logger logger = Logger.getLogger(ConnectionViewImpl.class.getName());

	@UiTemplate("ConnectionView.ui.xml")
	interface ConnectionViewUiBinder extends UiBinder<Widget, ConnectionViewImpl> {}
	private static ConnectionViewUiBinder uiBinder = GWT.create(ConnectionViewUiBinder.class);
	
	@UiField ConditionalPanel connectionControlsPanel;
	
	@UiField
	HTMLPanel connectionsContainer;
	
	@UiField
	HTMLPanel connectionContainer;
	
	@UiField
	HTMLPanel connectionDescriptionContainer;
	
	@UiField Label email;
	
	@UiField Label url;
	
	@UiField Label emailLabel;
	@UiField Label urlLabel;
	
	@UiField
	RaphaelButtonWidget addConnectionButton;

	@UiField
	RaphaelButtonWidget deleteConnectionButton;

	@UiField
	RaphaelButtonWidget editConnectionButton;

	private Presenter presenter;

	public ConnectionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		connectionContainer.setVisible(false);
		emailLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_email));
		urlLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_url));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}
	
	@UiHandler("addConnectionButton")
	void onAddConnectionButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddConnectionButtonClicked();
		}
	}

	@UiHandler("deleteConnectionButton")
	void onDeleteConnectionButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteConnectionButtonClicked();
		}
	}

	@UiHandler("editConnectionButton")
	void onEditConnectionButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditConnectionButtonClicked();
		}
	}

	@Override
	public void displayConnectionGroupConnections() {
		logger.log(Level.INFO, "displayConnectionGroupConnections");
		ItemListPresenter itemListPresenter = presenter.getConnectionGroupConnectionListPresenter();
		itemListPresenter.setItemListDTO(presenter.getConnectionGroupConnectionsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(connectionsContainer);
	}

	@Override
	public void displayConnection() {
		logger.log(Level.INFO, "displayConnection: " + presenter.getConnectionDTO().getAuthorDisplayName() + " - " + presenter.getConnectionDTO().getDescriptionContent().getName());
		connectionDescriptionContainer.clear();
		if (Utils.hasText(presenter.getConnectionDTO().getEmail())) {
			email.setText(presenter.getConnectionDTO().getEmail());
			emailLabel.setVisible(true);
			email.setVisible(true);
		} else {
			emailLabel.setVisible(false);
			email.setVisible(false);
		}
		if (Utils.hasText(presenter.getConnectionDTO().getUrl())) {
			url.setText(presenter.getConnectionDTO().getUrl());
			urlLabel.setVisible(true);
			url.setVisible(true);
		} else {
			urlLabel.setVisible(false);
			url.setVisible(false);
		}
		connectionContainer.setVisible(true);
		url.setText(presenter.getConnectionDTO().getUrl());
		//TODO display connection specific data, not in description
		presenter.getConnectionContentPresenter().setContentDTO(presenter.getConnectionDTO().getDescriptionContent());
		presenter.getConnectionContentPresenter().go(connectionDescriptionContainer);
		refreshControlsVisibility();
	}

	@Override
	public void displayConnectionGroups() {
		logger.log(Level.INFO, "displayConnectionGroups");
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getConnectionGroupsItemListDTO(), ItemListType.noPaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void reset(int level) {
		if (level > 1) {
			connectionsContainer.clear();
		}
		if (level > 0) {
			connectionContainer.setVisible(false);
			connectionDescriptionContainer.clear();
		}
	}

	@Override
	public void refreshControlsVisibility() {
		connectionControlsPanel.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		addConnectionButton.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		editConnectionButton.setVisible(presenter.getConnectionDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getConnectionDTO().getAuthorUsername()));
		deleteConnectionButton.setVisible(LocalStorage.getInstance().isUserModerator() || (presenter.getConnectionDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getConnectionDTO().getAuthorUsername())));
	}
}