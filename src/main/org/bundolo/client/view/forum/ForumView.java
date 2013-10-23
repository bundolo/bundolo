package org.bundolo.client.view.forum;

import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.ui.Widget;

public interface ForumView {

	public interface Presenter {
		ItemListDTO getForumGroupsItemListDTO();
		ItemListDTO getForumGroupTopicsItemListDTO();
		ItemListDTO getForumTopicPostsItemListDTO();
		void setForumGroupContentDTO(ContentDTO contentDTO);
		void setForumTopicContentDTO(ContentDTO contentDTO);
		ItemListPresenter getForumTopicListPresenter();
		ItemListPresenter getForumPostListPresenter();
		void onAddForumPostButtonClicked();
		void onDeleteForumPostButtonClicked();
		void onEditForumPostButtonClicked();
	}

	void setPresenter(Presenter presenter);
	void displayForumTopics();
	void displayForumGroups();
	void displayForumPosts();
	void refreshControlsVisibility();
	Widget asWidget();
} 