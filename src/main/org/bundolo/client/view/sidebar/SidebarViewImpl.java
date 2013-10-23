package org.bundolo.client.view.sidebar;

import java.util.logging.Logger;

import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SidebarViewImpl extends Composite implements SidebarView {

	private static final Logger logger = Logger.getLogger(SidebarViewImpl.class.getName());

	@UiTemplate("SidebarView.ui.xml")
	interface SidebarViewUiBinder extends UiBinder<Widget, SidebarViewImpl> {}
	private static SidebarViewUiBinder uiBinder = GWT.create(SidebarViewUiBinder.class);
	
	@UiField
	HTMLPanel newTextsSidebarContainer;
	
	@UiField
	HTMLPanel authorsSidebarContainer;
	
	@UiField
	HTMLPanel newsSidebarContainer;
	
	@UiField
	HTMLPanel newContestsSidebarContainer;
	
	@UiField
	HTMLPanel newConnectionsSidebarContainer;
	
	@UiField
	HTMLPanel newForumPostsSidebarContainer;
	
	@UiField
	HTMLPanel newEpisodesSidebarContainer;
	
	@UiField Label newTextsSidebarHeaderLabel;
	@UiField Label authorsSidebarHeaderLabel;
	@UiField Label newsSidebarHeaderLabel;
	@UiField Label newContestsSidebarHeaderLabel;
	@UiField Label newConnectionsSidebarHeaderLabel;
	@UiField Label newForumPostsSidebarHeaderLabel;
	@UiField Label newEpisodesSidebarHeaderLabel;

	private Presenter presenter;

	public SidebarViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		//TODO labels
//		newTextsSidebarHeaderLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_text));
//		authorsSidebarHeaderLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.avatar));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public Widget asWidget() {
		return this;
	}
	
	@Override
	public void displayAuthors() {
		authorsSidebarHeaderLabel.setText(presenter.getAuthorsItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getAuthorsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getAuthorsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(authorsSidebarContainer);
	}
	
	@Override
	public void displayNewTexts() {
		newTextsSidebarHeaderLabel.setText(presenter.getNewTextsItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getNewTextsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getNewTextsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(newTextsSidebarContainer);
	}

	@Override
	public void displayNews() {
		newsSidebarHeaderLabel.setText(presenter.getNewsItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getNewsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getNewsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(newsSidebarContainer);
	}

	@Override
	public void displayNewForumPosts() {
		newForumPostsSidebarHeaderLabel.setText(presenter.getNewForumPostsItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getNewForumPostsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getNewForumPostsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(newForumPostsSidebarContainer);
	}

	@Override
	public void displayNewContests() {
		newContestsSidebarHeaderLabel.setText(presenter.getNewContestsItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getNewContestsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getNewContestsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(newContestsSidebarContainer);
	}

	@Override
	public void displayNewConnections() {
		newConnectionsSidebarHeaderLabel.setText(presenter.getNewConnectionsItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getNewConnectionsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getNewConnectionsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(newConnectionsSidebarContainer);
	}

	@Override
	public void displayNewEpisodes() {
		newEpisodesSidebarHeaderLabel.setText(presenter.getNewEpisodesItemListDTO().getDescriptionContent().getName());
		ItemListPresenter itemListPresenter = presenter.getNewEpisodesListPresenter();
		itemListPresenter.setItemListDTO(presenter.getNewEpisodesItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(newEpisodesSidebarContainer);
	}

}