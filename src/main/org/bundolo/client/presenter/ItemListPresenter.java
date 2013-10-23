package org.bundolo.client.presenter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.list.AddItemListEvent;
import org.bundolo.client.event.list.EditItemListEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ConnectionServiceAsync;
import org.bundolo.shared.services.ContentServiceAsync;
import org.bundolo.shared.services.ContestServiceAsync;
import org.bundolo.shared.services.ItemListServiceAsync;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class ItemListPresenter implements Presenter, ItemListView.Presenter {
	
	private static final Logger logger = Logger.getLogger(ItemListPresenter.class.getName());

	private ItemListDTO itemListDTO;
	private ItemListType itemListType;
	//private List<?> listItems;
	private final ItemListServiceAsync itemListService;
	private final ContentServiceAsync contentService;
	private final UserProfileServiceAsync userProfileService;
	private final ContestServiceAsync contestService;
	private final ConnectionServiceAsync connectionService;
	private final HandlerManager eventBus;
	private final ItemListView view;
	private int itemListCount;
	private AsyncDataProvider<ContentDTO> provider;
	private AsyncDataProvider dataProvider;

	public ItemListPresenter(ItemListServiceAsync itemListService, ContentServiceAsync contentService, UserProfileServiceAsync userProfileService, ContestServiceAsync contestService, ConnectionServiceAsync connectionService, 
			HandlerManager eventBus, ItemListView view) {
		this.itemListService = itemListService;
		this.contentService = contentService;
		this.userProfileService = userProfileService;
		this.contestService = contestService;
		this.connectionService = connectionService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void go(final HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	private void bind() {
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.item_list_comment.equals(event.getUpdatedContent().getKind())) {
					view.displayItemList(itemListType);
					Utils.setHistoryTokenIfNeeded(PresenterName.lists);
				}
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public void onAddItemListButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addList.name());
		eventBus.fireEvent(new AddItemListEvent());
	}

	@Override
	public void onDeleteItemListButtonClicked() {
		if ((itemListDTO != null) && (itemListDTO.getItemListId() != null)) {
			deleteCurrentList();
		}
	}

	@Override
	public void onEditItemListButtonClicked() {
		if ((itemListDTO != null) && (itemListDTO.getItemListId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editList.name());
			eventBus.fireEvent(new EditItemListEvent(itemListDTO));
		}
	}

	@Override
	public ItemListDTO getItemList() {
		return itemListDTO;
	}
	
	private void deleteCurrentList() {
		itemListService.deleteItemList(itemListDTO.getItemListId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_deleted)));
			}}
		);
	}

	@Override
	public void setItemListDTO(ItemListDTO itemListDTO, final ItemListType itemListType) {
		this.itemListDTO = itemListDTO;
		this.itemListType = itemListType;
		if(itemListDTO != null) {
			switch (itemListDTO.getKind()) {
			case Content:
				contentService.findItemListContentsCount(itemListDTO.getQuery(), new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error getting list items count: ", caught);
						eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
					}
					@Override
					public void onSuccess(Integer result) {
						itemListCount = result;
						view.displayItemList(itemListType);
						setDataProvider(itemListCount);
					}}
				);
				break;
			case UserProfile:
				userProfileService.findItemListUsersCount(itemListDTO.getQuery(), new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error getting list items count: ", caught);
						eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
					}
					@Override
					public void onSuccess(Integer result) {
						itemListCount = result;
						view.displayItemList(itemListType);
						setDataProvider(itemListCount);
					}}
				);
				break;
			case Contest:
				contestService.findItemListContestsCount(itemListDTO.getQuery(), new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error getting list items count: ", caught);
						eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
					}
					@Override
					public void onSuccess(Integer result) {
						itemListCount = result;
						view.displayItemList(itemListType);
						setDataProvider(itemListCount);
					}}
				);
				break;
			case Connection:
				connectionService.findItemListConnectionsCount(itemListDTO.getQuery(), new AsyncCallback<Integer>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error getting list items count: ", caught);
						eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
					}
					@Override
					public void onSuccess(Integer result) {
						itemListCount = result;
						view.displayItemList(itemListType);
						setDataProvider(itemListCount);
					}}
				);
				break;
			}
		}
	}
	
	private void setDataProvider(final int itemListCount) {
		logger.log(Level.FINE, "itemListCount: " + itemListCount);
		provider = new AsyncDataProvider() {
	        @Override
	        protected void onRangeChanged(HasData display) {
	          final int start = display.getVisibleRange().getStart();
	          int end = start + display.getVisibleRange().getLength();
	          end = end >= itemListCount ? itemListCount : end;
	          
	          logger.log(Level.FINE, "start: " + start + ", end: " + end);
	          switch (itemListDTO.getKind()) {
				case Content:
				contentService.findItemListContents(itemListDTO.getQuery(), start, end, new AsyncCallback<List<ContentDTO>>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error getting list items: ", caught);
						eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
					}
					@Override
					public void onSuccess(List<ContentDTO> result) {
						updateRowData(start, result);
						//view.displayItemList();
					}}
				);
				break;
				case UserProfile:
					userProfileService.findItemListUsers(itemListDTO.getQuery(), start, end, new AsyncCallback<List<UserDTO>>() {
						@Override
						public void onFailure(Throwable caught) {
							logger.log(Level.SEVERE, "Error getting list items: ", caught);
							eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
						}
						@Override
						public void onSuccess(List<UserDTO> result) {
							updateRowData(start, result);
							//view.displayItemList();
						}}
					);
					break;
				case Contest:
					contestService.findItemListContests(itemListDTO.getQuery(), start, end, new AsyncCallback<List<ContestDTO>>() {
						@Override
						public void onFailure(Throwable caught) {
							logger.log(Level.SEVERE, "Error getting list items: ", caught);
							eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
						}
						@Override
						public void onSuccess(List<ContestDTO> result) {
							updateRowData(start, result);
							//view.displayItemList();
						}}
					);
					break;
				case Connection:
					connectionService.findItemListConnections(itemListDTO.getQuery(), start, end, new AsyncCallback<List<ConnectionDTO>>() {
						@Override
						public void onFailure(Throwable caught) {
							logger.log(Level.SEVERE, "Error getting list items: ", caught);
							eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
						}
						@Override
						public void onSuccess(List<ConnectionDTO> result) {
							updateRowData(start, result);
							//view.displayItemList();
						}}
					);
					break;
	          }
	        }
		};
		provider.updateRowCount(itemListCount, true);
		provider.addDataDisplay(view.getDataHolder());
	}
	
	@Override
	public int getItemListCount() {
		return itemListCount;
	}

	@Override
	public void itemSelected(Object selectedItem) {
		//TODO history item should be localized
		if (selectedItem != null) {
			if (selectedItem instanceof ContentDTO) {
				ContentDTO selectedContent = (ContentDTO) selectedItem;
				switch (selectedContent.getKind()) {
				case forum_group:
					Utils.setHistoryTokenIfNeeded(PresenterName.forum);
					ForumPresenter forumGroupPresenter = (ForumPresenter)LocalStorage.getInstance().getPresenter(PresenterName.forum.name());
					forumGroupPresenter.setForumGroupContentDTO(selectedContent);
					break;
				case forum_topic:
					Utils.setHistoryTokenIfNeeded(PresenterName.forum);
					ForumPresenter forumPostPresenter = (ForumPresenter)LocalStorage.getInstance().getPresenter(PresenterName.forum.name());
					forumPostPresenter.setForumTopicContentDTO(selectedContent);
					break;
				case forum_post:
					contentService.findContent(selectedContent.getParentContentId(), new AsyncCallback<ContentDTO>() {
						@Override
						public void onFailure(Throwable caught) {
							logger.log(Level.SEVERE, "Error getting post topic: ", caught);
							eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
						}
						@Override
						public void onSuccess(ContentDTO result) {
							Utils.setHistoryTokenIfNeeded(PresenterName.forum);
							ForumPresenter forumTopicPresenter = (ForumPresenter)LocalStorage.getInstance().getPresenter(PresenterName.forum.name());
							forumTopicPresenter.setForumTopicContentDTO(result);
						}}
					);
					break;
				case episode_group:
					Utils.setHistoryTokenIfNeeded(PresenterName.serials);
					SerialPresenter serialsPresenter = (SerialPresenter)LocalStorage.getInstance().getPresenter(PresenterName.serials.name());
					serialsPresenter.setSerialContentDTO(selectedContent);
					break;
				case episode:
					Utils.setHistoryTokenIfNeeded(PresenterName.serials);
					SerialPresenter serialEpisodePresenter = (SerialPresenter)LocalStorage.getInstance().getPresenter(PresenterName.serials.name());
					serialEpisodePresenter.setEpisodeContentDTO(selectedContent);					
					break;
				case text:
					Utils.setHistoryTokenIfNeeded(PresenterName.texts);
					TextPresenter textPresenter = (TextPresenter)LocalStorage.getInstance().getPresenter(PresenterName.texts.name());
					textPresenter.setTextContentDTO(selectedContent);
					break;
				case news:
					Utils.setHistoryTokenIfNeeded(PresenterName.news);
					NewsPresenter newsPresenter = (NewsPresenter)LocalStorage.getInstance().getPresenter(PresenterName.news.name());
					newsPresenter.setNewsContentDTO(selectedContent);
					break;
				case connection_group:
					Utils.setHistoryTokenIfNeeded(PresenterName.connections);
					ConnectionPresenter connectionPresenter = (ConnectionPresenter)LocalStorage.getInstance().getPresenter(PresenterName.connections.name());
					connectionPresenter.setConnectionGroupContentDTO(selectedContent);
					break;
				}
			} else if (selectedItem instanceof UserDTO) {
				Utils.setHistoryTokenIfNeeded(PresenterName.authors);
				UserDTO selectedUser = (UserDTO) selectedItem;
				AuthorPresenter authorPresenter = (AuthorPresenter)LocalStorage.getInstance().getPresenter(PresenterName.authors.name());
				authorPresenter.setAuthorUserDTO(selectedUser);
			} else if (selectedItem instanceof ContestDTO) {
				Utils.setHistoryTokenIfNeeded(PresenterName.contests);
				ContestDTO selectedContest = (ContestDTO) selectedItem;
				ContestPresenter contestPresenter = (ContestPresenter)LocalStorage.getInstance().getPresenter(PresenterName.contests.name());
				contestPresenter.setContestDTO(selectedContest);
			} else if (selectedItem instanceof ConnectionDTO) {
				Utils.setHistoryTokenIfNeeded(PresenterName.connections);
				ConnectionDTO selectedConnection = (ConnectionDTO) selectedItem;
				ConnectionPresenter connectionPresenter = (ConnectionPresenter)LocalStorage.getInstance().getPresenter(PresenterName.connections.name());
				connectionPresenter.setConnectionDTO(selectedConnection);
			}
		}
	}
}