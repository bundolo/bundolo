package org.bundolo.client.view.serial;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.client.widget.ConditionalPanel;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageKindType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class SerialViewImpl extends Composite implements SerialView {

	private static final Logger logger = Logger.getLogger(SerialViewImpl.class.getName());

	@UiTemplate("SerialView.ui.xml")
	interface SerialViewUiBinder extends UiBinder<Widget, SerialViewImpl> {}
	private static SerialViewUiBinder uiBinder = GWT.create(SerialViewUiBinder.class);
	
	@UiField ConditionalPanel serialControlsPanel;
	@UiField ConditionalPanel episodeControlsPanel;
	
	@UiField
	HTMLPanel episodesContainer;
	
	@UiField
	HTML serialDescription;
	
	@UiField
	HTMLPanel episodeContainer;
	
	@UiField
	RaphaelButtonWidget addSerialButton;

	@UiField
	RaphaelButtonWidget deleteSerialButton;

	@UiField
	RaphaelButtonWidget editSerialButton;
	
	@UiField
	RaphaelButtonWidget addEpisodeButton;

	@UiField
	RaphaelButtonWidget deleteEpisodeButton;

	@UiField
	RaphaelButtonWidget editEpisodeButton;

	private Presenter presenter;

	public SerialViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}
	
	@UiHandler("addSerialButton")
	void onAddSerialButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddSerialButtonClicked();
		}
	}

	@UiHandler("deleteSerialButton")
	void onDeleteSerialButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteSerialButtonClicked();
		}
	}

	@UiHandler("editSerialButton")
	void onEditSerialButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditSerialButtonClicked();
		}
	}
	
	@UiHandler("addEpisodeButton")
	void onAddEpisodeButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddEpisodeButtonClicked();
		}
	}

	@UiHandler("deleteEpisodeButton")
	void onDeleteEpisodeButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteEpisodeButtonClicked();
		}
	}

	@UiHandler("editEpisodeButton")
	void onEditEpisodeButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditEpisodeButtonClicked();
		}
	}

	@Override
	public void displaySerialEpisodes() {
		episodeContainer.clear();
		serialDescription.setHTML(presenter.getSerialContentDTO().getText());
		presenter.getSerialEpisodeListPresenter().setItemListDTO(presenter.getSerialEpisodesItemListDTO(), ItemListType.simplePaging);
		presenter.getSerialEpisodeListPresenter().go(episodesContainer);
		refreshControlsVisibility();
	}

	@Override
	public void displaySerials() {
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getSerialsItemListDTO(), ItemListType.simplePaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void displayEpisode() {
		serialDescription.setHTML("");
		episodeContainer.clear();
		ContentPresenter contentPresenter = (ContentPresenter)LocalStorage.getInstance().getPresenter(PresenterName.content.name());
		contentPresenter.setContentDTO(presenter.getEpisodeContentDTO());		
		contentPresenter.go(episodeContainer);
		refreshControlsVisibility();
	}

	@Override
	public void refreshControlsVisibility() {
		serialControlsPanel.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		episodeControlsPanel.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		//TODO more logic here.
		//serial delete is not so simple because of other people's episodes
		//add episode if previous is done
		//edit episode if there is no next
		//delete episode if it's not done
		addEpisodeButton.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		editEpisodeButton.setVisible(presenter.getEpisodeContentDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getEpisodeContentDTO().getAuthorUsername()));
		deleteEpisodeButton.setVisible(LocalStorage.getInstance().isUserModerator() || (presenter.getEpisodeContentDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getEpisodeContentDTO().getAuthorUsername())));
		addSerialButton.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		editSerialButton.setVisible(presenter.getSerialContentDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getSerialContentDTO().getAuthorUsername()));
		deleteSerialButton.setVisible(LocalStorage.getInstance().isUserModerator() || (presenter.getSerialContentDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getSerialContentDTO().getAuthorUsername())));
	}
}