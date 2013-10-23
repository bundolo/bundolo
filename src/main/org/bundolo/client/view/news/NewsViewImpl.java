package org.bundolo.client.view.news;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class NewsViewImpl extends Composite implements NewsView {

	private static final Logger logger = Logger.getLogger(NewsViewImpl.class.getName());

	@UiTemplate("NewsView.ui.xml")
	interface TextViewUiBinder extends UiBinder<Widget, NewsViewImpl> {}
	private static TextViewUiBinder uiBinder = GWT.create(TextViewUiBinder.class);
	
	@UiField ConditionalPanel newsControlsPanel;
	
	@UiField
	HTMLPanel singleNewsContainer;
	
	@UiField
	RaphaelButtonWidget addNewsButton;

	@UiField
	RaphaelButtonWidget deleteNewsButton;

	@UiField
	RaphaelButtonWidget editNewsButton;

	private Presenter presenter;

	public NewsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}
	
	@UiHandler("addNewsButton")
	void onAddNewsButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddNewsButtonClicked();
		}
	}

	@UiHandler("deleteNewsButton")
	void onDeleteNewsButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteNewsButtonClicked();
		}
	}

	@UiHandler("editNewsButton")
	void onEditNewsButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditNewsButtonClicked();
		}
	}

	@Override
	public void displayNews() {
		logger.log(Level.INFO, "displayNews");
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getNewsItemListDTO(), ItemListType.simplePaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void displaySingleNews() {
		logger.log(Level.INFO, "displaySingleNews: " + presenter.getNewsContentDTO().getAuthorDisplayName() + " - " + presenter.getNewsContentDTO().getName());
		singleNewsContainer.clear();
		presenter.getNewsContentPresenter().setContentDTO(presenter.getNewsContentDTO());
		presenter.getNewsContentPresenter().go(singleNewsContainer);
		refreshControlsVisibility();
	}

	@Override
	public void refreshControlsVisibility() {
		newsControlsPanel.setVisible(LocalStorage.getInstance().isUserModerator());
	}
}