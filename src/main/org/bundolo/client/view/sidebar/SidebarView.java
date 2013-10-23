package org.bundolo.client.view.sidebar;

import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface SidebarView {

	public interface Presenter {
		ItemListDTO getNewTextsItemListDTO();
		ItemListDTO getAuthorsItemListDTO();
		ItemListDTO getNewsItemListDTO();
		ItemListDTO getNewForumPostsItemListDTO();
		ItemListDTO getNewContestsItemListDTO();
		ItemListDTO getNewConnectionsItemListDTO();
		ItemListDTO getNewEpisodesItemListDTO();
		ItemListPresenter getNewTextsListPresenter();
		ItemListPresenter getAuthorsListPresenter();
		ItemListPresenter getNewsListPresenter();
		ItemListPresenter getNewForumPostsListPresenter();
		ItemListPresenter getNewContestsListPresenter();
		ItemListPresenter getNewConnectionsListPresenter();
		ItemListPresenter getNewEpisodesListPresenter();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	void displayAuthors();
	void displayNewTexts();
	void displayNews();
	void displayNewForumPosts();
	void displayNewContests();
	void displayNewConnections();
	void displayNewEpisodes();
} 