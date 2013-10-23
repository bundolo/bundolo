package org.bundolo.client.view.forum;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;
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

public class ForumViewImpl extends Composite implements ForumView {

	private static final Logger logger = Logger.getLogger(ForumViewImpl.class.getName());

	@UiTemplate("ForumView.ui.xml")
	interface ForumViewUiBinder extends UiBinder<Widget, ForumViewImpl> {}
	private static ForumViewUiBinder uiBinder = GWT.create(ForumViewUiBinder.class);
	
	@UiField
	HTMLPanel forumTopicsContainer;
	
	@UiField
	HTMLPanel forumPostsContainer;
	
	@UiField
	HTMLPanel forumControls;
	
	@UiField
	RaphaelButtonWidget addForumPostButton;

	@UiField
	RaphaelButtonWidget deleteForumPostButton;

	@UiField
	RaphaelButtonWidget editForumPostButton;

	private Presenter presenter;

	public ForumViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}
	
	@UiHandler("addForumPostButton")
	void onAddForumPostButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddForumPostButtonClicked();
		}
	}

	@UiHandler("deleteForumPostButton")
	void onDeleteForumPostButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteForumPostButtonClicked();
		}
	}

	@UiHandler("editForumPostButton")
	void onEditForumPostButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditForumPostButtonClicked();
		}
	}

	@Override
	public void displayForumTopics() {
		forumPostsContainer.clear();
		presenter.getForumTopicListPresenter().setItemListDTO(presenter.getForumGroupTopicsItemListDTO(), ItemListType.simpleCellTablePaging);
		presenter.getForumTopicListPresenter().go(forumTopicsContainer);
	}

	@Override
	public void displayForumGroups() {
		forumTopicsContainer.clear();
		forumPostsContainer.clear();
		ItemListPresenter itemListPresenter = (ItemListPresenter)LocalStorage.getInstance().getPresenter(PageKindType.lists.name());
		itemListPresenter.setItemListDTO(presenter.getForumGroupsItemListDTO(), ItemListType.simpleCellTable);
		((PagePresenter)LocalStorage.getInstance().getPresenter(PresenterName.page.name())).displayPageSpecificList();
	}

	@Override
	public void displayForumPosts() {
		presenter.getForumPostListPresenter().setItemListDTO(presenter.getForumTopicPostsItemListDTO(), ItemListType.fullCellTable);
		presenter.getForumPostListPresenter().go(forumPostsContainer);
		refreshControlsVisibility();
	}

	@Override
	public void refreshControlsVisibility() {
//		forumControls.setVisible(true);
//		addForumPostButton.setVisible(true);
		//TODO there should be a timer for edit and delete for the author
//		editForumPostButton.setVisible();
		deleteForumPostButton.setVisible(LocalStorage.getInstance().isUserModerator());
	}

}