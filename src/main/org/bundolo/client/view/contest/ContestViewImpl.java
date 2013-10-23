package org.bundolo.client.view.contest;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class ContestViewImpl extends Composite implements ContestView {

	private static final Logger logger = Logger.getLogger(ContestViewImpl.class.getName());

	@UiTemplate("ContestView.ui.xml")
	interface ContestViewUiBinder extends UiBinder<Widget, ContestViewImpl> {}
	private static ContestViewUiBinder uiBinder = GWT.create(ContestViewUiBinder.class);
	
	@UiField ConditionalPanel contestControlsPanel;
	
	@UiField
	HTMLPanel contestContainer;
	
	@UiField
	HTMLPanel contestDescriptionContainer;
	
	@UiField Label expirationDateLabel;
	@UiField Label expirationDate;
	
	@UiField
	RaphaelButtonWidget addContestButton;
	@UiField
	RaphaelButtonWidget deleteContestButton;
	@UiField
	RaphaelButtonWidget editContestButton;

	private Presenter presenter;

	public ContestViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		contestContainer.setVisible(false);
		expirationDateLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_expiration_date));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public void displayContests() {
		logger.log(Level.INFO, "displayContests");
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getContestsItemListDTO(), ItemListType.simplePaging);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void displayContest() {
		logger.log(Level.INFO, "displayContest: " + presenter.getContestDTO().getAuthorDisplayName() + " - " + presenter.getContestDTO().getDescriptionContent().getName());
		contestDescriptionContainer.clear();
//		contestContainer.clear();
		//TODO display contest specific data, not in description
		if (presenter.getContestDTO().getExpirationDate() != null) {
			expirationDate.setText(LocalStorage.getInstance().getDateTimeformat().format(presenter.getContestDTO().getExpirationDate()));
		} else {
			expirationDate.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_no_expiration_date));
		}
		contestContainer.setVisible(true);
		presenter.getContestContentPresenter().setContentDTO(presenter.getContestDTO().getDescriptionContent());
		presenter.getContestContentPresenter().go(contestDescriptionContainer);
		refreshControlsVisibility();
	}
	
	@UiHandler("addContestButton")
	void onAddContestButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddContestButtonClicked();
		}
	}

	@UiHandler("deleteContestButton")
	void onDeleteContestButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteContestButtonClicked();
		}
	}

	@UiHandler("editContestButton")
	void onEditContestButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditContestButtonClicked();
		}
	}

	@Override
	public void reset(int level) {
		if (level > 0) {
			contestDescriptionContainer.clear();
			contestContainer.setVisible(false);
		}
	}

	@Override
	public void refreshControlsVisibility() {
		contestControlsPanel.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		addContestButton.setVisible(LocalStorage.getInstance().isUserLoggedIn());
		editContestButton.setVisible(presenter.getContestDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getContestDTO().getAuthorUsername()));
		deleteContestButton.setVisible(LocalStorage.getInstance().isUserModerator() || (presenter.getContestDTO() != null && LocalStorage.getInstance().isUserLoggedIn() && LocalStorage.getInstance().getUsername().equals(presenter.getContestDTO().getAuthorUsername())));
	}
}