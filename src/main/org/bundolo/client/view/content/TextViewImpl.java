package org.bundolo.client.view.content;

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

public class TextViewImpl extends Composite implements TextView {

	private static final Logger logger = Logger.getLogger(TextViewImpl.class.getName());

	@UiTemplate("TextView.ui.xml")
	interface TextViewUiBinder extends UiBinder<Widget, TextViewImpl> {}
	private static TextViewUiBinder uiBinder = GWT.create(TextViewUiBinder.class);
	
	@UiField ConditionalPanel textControlsPanel;
	
	@UiField
	HTMLPanel textContainer;
	
	@UiField
	RaphaelButtonWidget addTextButton;

	@UiField
	RaphaelButtonWidget deleteTextButton;

	@UiField
	RaphaelButtonWidget editTextButton;

	private Presenter presenter;

	public TextViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}
	
	@UiHandler("addTextButton")
	void onAddTextButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddTextButtonClicked();
		}
	}

	@UiHandler("deleteTextButton")
	void onDeleteTextButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteTextButtonClicked();
		}
	}

	@UiHandler("editTextButton")
	void onEditTextButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditTextButtonClicked();
		}
	}

	@Override
	public void displayTexts() {
		logger.log(Level.INFO, "displayTexts");
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getTextsItemListDTO(), ItemListType.simplePaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void displayText() {
		logger.log(Level.INFO, "displayText: " + presenter.getTextContentDTO().getAuthorDisplayName() + " - " + presenter.getTextContentDTO().getName());
		textContainer.clear();
		presenter.getTextContentPresenter().setContentDTO(presenter.getTextContentDTO());
		presenter.getTextContentPresenter().go(textContainer);
		refreshControlsVisibility();
	}

	@Override
	public void refreshControlsVisibility() {
		textControlsPanel.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		addTextButton.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		logger.log(Level.INFO, "presenter.getTextContentDTO(): " + presenter.getTextContentDTO());
		logger.log(Level.INFO, "LocalStorage.getInstance().isUserLoggedIn(): " + LocalStorage.getInstance().isUserLoggedIn());
		logger.log(Level.INFO, "LocalStorage.getInstance().getUsername(): " + LocalStorage.getInstance().getUsername());
//		logger.log(Level.INFO, "presenter.getTextContentDTO().getAuthorUsername(): " + presenter.getTextContentDTO().getAuthorUsername());
		
		editTextButton.setVisible(presenter.getTextContentDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getTextContentDTO().getAuthorUsername()));
		deleteTextButton.setVisible(LocalStorage.getInstance().isUserModerator() || (presenter.getTextContentDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getTextContentDTO().getAuthorUsername())));
	}
}