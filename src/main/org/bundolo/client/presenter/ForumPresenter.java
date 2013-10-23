package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.forum.AddForumPostEvent;
import org.bundolo.client.event.forum.EditForumPostEvent;
import org.bundolo.client.event.forum.ForumPostUpdatedEvent;
import org.bundolo.client.event.forum.ForumPostUpdatedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.forum.ForumView;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContentServiceAsync;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class ForumPresenter implements Presenter, ForumView.Presenter {
	
	private static final Logger logger = Logger.getLogger(ForumPresenter.class.getName());

	private final HandlerManager eventBus;
	private final ForumView view;
	private ItemListDTO forumGroupsItemListDTO;
	private ItemListDTO forumGroupTopicsItemListDTO;
	private ItemListDTO forumTopicPostsItemListDTO;
	private ContentDTO forumGroupContentDTO;
	private ContentDTO forumTopicContentDTO;
	private ItemListPresenter forumTopicListPresenter;
	private ItemListPresenter forumPostListPresenter;
	private final ItemListServiceAsync itemListService;
	private final ContentServiceAsync contentService;

	public ForumPresenter(ContentServiceAsync contentService, ItemListServiceAsync itemListService, HandlerManager eventBus, ForumView view) {
		this.contentService = contentService;
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ItemListView forumTopicItemListViewImpl = new ItemListViewImpl();
		forumTopicListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, forumTopicItemListViewImpl);
		ItemListView forumPostItemListViewImpl = new ItemListViewImpl();
		forumPostListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, forumPostItemListViewImpl);
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding view");
		container.add(view.asWidget());
		
		itemListService.findItemListByName(ItemListNameType.forum_groups.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				forumGroupsItemListDTO = result;
				view.displayForumGroups();
			}}
		);
	}
	
	private void doForumPostUpdated(ContentDTO contentDTO) {
		//TODO this is quite inefficient, also topic should be scrolled to the last page
		if (ContentKindType.forum_topic.equals(contentDTO.getKind())) {
			contentService.findContent(contentDTO.getParentContentId(), new AsyncCallback<ContentDTO>() {
				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, "Error getting forum parent: ", caught);
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
				}
				@Override
				public void onSuccess(ContentDTO result) {					
					setForumGroupContentDTO(result);
				}}
			);
		} else if (ContentKindType.forum_post.equals(contentDTO.getKind())) {
			contentService.findContent(contentDTO.getParentContentId(), new AsyncCallback<ContentDTO>() {
				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, "Error getting forum parent: ", caught);
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
				}
				@Override
				public void onSuccess(ContentDTO result) {					
					setForumTopicContentDTO(result);
				}}
			);
		}
		Utils.setHistoryTokenIfNeeded(PresenterName.forum);
	}
	private void bind() {
		eventBus.addHandler(ForumPostUpdatedEvent.TYPE, new ForumPostUpdatedEventHandler() {
			public void onForumPostUpdated(ForumPostUpdatedEvent event) {
				doForumPostUpdated(event.getUpdatedContent());
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public ItemListDTO getForumGroupsItemListDTO() {
		return forumGroupsItemListDTO;
	}

	@Override
	public ItemListDTO getForumGroupTopicsItemListDTO() {
		return forumGroupTopicsItemListDTO;
	}

	@Override
	public void setForumGroupContentDTO(ContentDTO contentDTO) {
		//logger.severe("setForumGroupContentDTO: " + contentDTO);
		this.forumGroupContentDTO = contentDTO;
		itemListService.findItemListByName(ItemListNameType.forum_group_topics.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE, forumGroupContentDTO.getContentId().toString()), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				forumGroupTopicsItemListDTO = result;
				view.displayForumTopics();
			}}
		);
	}

	@Override
	public ItemListPresenter getForumTopicListPresenter() {
		return forumTopicListPresenter;
	}

	@Override
	public ItemListDTO getForumTopicPostsItemListDTO() {
		return forumTopicPostsItemListDTO;
	}

	@Override
	public void setForumTopicContentDTO(ContentDTO contentDTO) {
		this.forumTopicContentDTO = contentDTO;
		itemListService.findItemListByName(ItemListNameType.forum_topic_posts.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE, forumTopicContentDTO.getContentId().toString()), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				forumTopicPostsItemListDTO = result;
				view.displayForumPosts();
			}}
		);
	}

	@Override
	public ItemListPresenter getForumPostListPresenter() {
		return forumPostListPresenter;
	}

	@Override
	public void onAddForumPostButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addForumPost.name());
		if ((forumTopicContentDTO != null) && (forumTopicContentDTO.getContentId() != null)) {
			eventBus.fireEvent(new AddForumPostEvent(forumTopicContentDTO));
		} else {
			eventBus.fireEvent(new AddForumPostEvent(forumGroupContentDTO));
		}		
	}

	@Override
	public void onDeleteForumPostButtonClicked() {
		if ((forumTopicContentDTO != null) && (forumTopicContentDTO.getContentId() != null)) {
			deleteCurrentForumPost();
		}
	}

	@Override
	public void onEditForumPostButtonClicked() {
		if ((forumTopicContentDTO != null) && (forumTopicContentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editForumPost.name());
			eventBus.fireEvent(new EditForumPostEvent(forumTopicContentDTO));
		}
	}
	
	private void deleteCurrentForumPost() {
		//TODO there should be add, remove and edit button on each post, so add, delete and edit should be more generic, now they are for topics
		contentService.deleteContent(forumTopicContentDTO.getContentId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting forum post: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_deleted)));
			}}
		);
	}

}
