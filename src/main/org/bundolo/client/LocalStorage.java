package org.bundolo.client;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.event.list.ItemListRefreshedEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.page.NavigationPagesLoadedEvent;
import org.bundolo.client.presenter.ActivateUserProfilePresenter;
import org.bundolo.client.presenter.AddUserProfilePresenter;
import org.bundolo.client.presenter.AuthorPresenter;
import org.bundolo.client.presenter.ConnectionPresenter;
import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.client.presenter.ContestPresenter;
import org.bundolo.client.presenter.EditCommentPresenter;
import org.bundolo.client.presenter.EditConnectionPresenter;
import org.bundolo.client.presenter.EditContestPresenter;
import org.bundolo.client.presenter.EditEpisodePresenter;
import org.bundolo.client.presenter.EditForumPostPresenter;
import org.bundolo.client.presenter.EditItemListPresenter;
import org.bundolo.client.presenter.EditLabelsPresenter;
import org.bundolo.client.presenter.EditMessagePresenter;
import org.bundolo.client.presenter.EditNewsPresenter;
import org.bundolo.client.presenter.EditPagePresenter;
import org.bundolo.client.presenter.EditSerialPresenter;
import org.bundolo.client.presenter.EditTextPresenter;
import org.bundolo.client.presenter.EditUserProfilePresenter;
import org.bundolo.client.presenter.ForumPresenter;
import org.bundolo.client.presenter.HomePresenter;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.LoginPresenter;
import org.bundolo.client.presenter.NewsPresenter;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.presenter.Presenter;
import org.bundolo.client.presenter.SerialPresenter;
import org.bundolo.client.presenter.SidebarPresenter;
import org.bundolo.client.presenter.StatusPresenter;
import org.bundolo.client.presenter.TextPresenter;
import org.bundolo.client.presenter.UserProfilePresenter;
import org.bundolo.client.resource.CustomValidationMessages;
import org.bundolo.client.resource.IconResource;
import org.bundolo.client.resource.MessageResource;
import org.bundolo.client.resource.StandardValidationMessagesImpl;
import org.bundolo.client.view.connection.ConnectionView;
import org.bundolo.client.view.connection.ConnectionViewImpl;
import org.bundolo.client.view.connection.EditConnectionView;
import org.bundolo.client.view.connection.EditConnectionViewImpl;
import org.bundolo.client.view.content.ContentView;
import org.bundolo.client.view.content.ContentViewImpl;
import org.bundolo.client.view.content.EditCommentView;
import org.bundolo.client.view.content.EditCommentViewImpl;
import org.bundolo.client.view.content.EditLabelsView;
import org.bundolo.client.view.content.EditLabelsViewImpl;
import org.bundolo.client.view.content.EditTextView;
import org.bundolo.client.view.content.EditTextViewImpl;
import org.bundolo.client.view.content.TextView;
import org.bundolo.client.view.content.TextViewImpl;
import org.bundolo.client.view.contest.ContestView;
import org.bundolo.client.view.contest.ContestViewImpl;
import org.bundolo.client.view.contest.EditContestView;
import org.bundolo.client.view.contest.EditContestViewImpl;
import org.bundolo.client.view.forum.EditForumPostView;
import org.bundolo.client.view.forum.EditForumPostViewImpl;
import org.bundolo.client.view.forum.ForumView;
import org.bundolo.client.view.forum.ForumViewImpl;
import org.bundolo.client.view.home.HomeView;
import org.bundolo.client.view.home.HomeViewImpl;
import org.bundolo.client.view.list.EditItemListView;
import org.bundolo.client.view.list.EditItemListViewImpl;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.client.view.news.EditNewsView;
import org.bundolo.client.view.news.EditNewsViewImpl;
import org.bundolo.client.view.news.NewsView;
import org.bundolo.client.view.news.NewsViewImpl;
import org.bundolo.client.view.page.EditPageView;
import org.bundolo.client.view.page.EditPageViewImpl;
import org.bundolo.client.view.page.PageView;
import org.bundolo.client.view.page.PageViewImpl;
import org.bundolo.client.view.page.StatusView;
import org.bundolo.client.view.page.StatusViewImpl;
import org.bundolo.client.view.serial.EditEpisodeView;
import org.bundolo.client.view.serial.EditEpisodeViewImpl;
import org.bundolo.client.view.serial.EditSerialView;
import org.bundolo.client.view.serial.EditSerialViewImpl;
import org.bundolo.client.view.serial.SerialView;
import org.bundolo.client.view.serial.SerialViewImpl;
import org.bundolo.client.view.sidebar.SidebarView;
import org.bundolo.client.view.sidebar.SidebarViewImpl;
import org.bundolo.client.view.user.ActivateUserProfileView;
import org.bundolo.client.view.user.ActivateUserProfileViewImpl;
import org.bundolo.client.view.user.AddUserProfileView;
import org.bundolo.client.view.user.AddUserProfileViewImpl;
import org.bundolo.client.view.user.AuthorView;
import org.bundolo.client.view.user.AuthorViewImpl;
import org.bundolo.client.view.user.EditMessageView;
import org.bundolo.client.view.user.EditMessageViewImpl;
import org.bundolo.client.view.user.EditUserProfileView;
import org.bundolo.client.view.user.EditUserProfileViewImpl;
import org.bundolo.client.view.user.LoginView;
import org.bundolo.client.view.user.LoginViewImpl;
import org.bundolo.client.view.user.UserProfileView;
import org.bundolo.client.view.user.UserProfileViewImpl;
import org.bundolo.client.raphael.Navigation;
import org.bundolo.client.widget.NotificationPopup;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ConnectionService;
import org.bundolo.shared.services.ConnectionServiceAsync;
import org.bundolo.shared.services.ContentService;
import org.bundolo.shared.services.ContentServiceAsync;
import org.bundolo.shared.services.ContestService;
import org.bundolo.shared.services.ContestServiceAsync;
import org.bundolo.shared.services.ItemListService;
import org.bundolo.shared.services.ItemListServiceAsync;
import org.bundolo.shared.services.PageService;
import org.bundolo.shared.services.PageServiceAsync;
import org.bundolo.shared.services.UserProfileService;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.maydu.gwt.validation.client.i18n.StandardValidationMessages;
//import com.google.gwt.dev.util.collect.HashSet;

public class LocalStorage {

	private static Logger logger = Logger.getLogger(LocalStorage.class.getName());

	private static LocalStorage localStorage = null;

	private static ContentServiceAsync contentService;
	private static PageServiceAsync pageService;
	private static UserProfileServiceAsync userProfileService;
	private static ItemListServiceAsync itemListService;
	private static ContestServiceAsync contestService;
	private static ConnectionServiceAsync connectionService;
	private static HandlerManager eventBus;
	private static NotificationPopup notificationPopup;
	private static CustomValidationMessages validationMessages;
	private static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(Constants.DEFAULT_DATE_FORMAT);
	private static HashSet<Long> ratedContentIds = new HashSet<Long>();
	private static Navigation navigation;
	
	public static enum PresenterName {
		page, addPage, editPage, content, addComment, editComment, user, addUser, editUser, activate, home, login, status, lists, addList, editList, 
		forum, addForumPost, editForumPost, serials, addSerial, editSerial, addEpisode, editEpisode, authors, texts, addText, editText, news, addNews, editNews, 
		contests, addContest, editContest, connections, addConnection, editConnection, addMessage, sidebar, editLabels
	};

	private static Map<PresenterName, Presenter> presenters = new EnumMap<PresenterName, Presenter>(PresenterName.class);
	private static MessageResource messageResource = null;
	private static IconResource iconResource;
	private static List<Object> navigationPages;
	private static Map<ItemListNameType, ItemListDTO> itemLists = new EnumMap<ItemListNameType, ItemListDTO>(ItemListNameType.class);

	private LocalStorage() {
		logger.log(Level.FINE, "constructor");
		contentService = GWT.create(ContentService.class);
		pageService = GWT.create(PageService.class);
		userProfileService = GWT.create(UserProfileService.class);
		itemListService = GWT.create(ItemListService.class);
		contestService = GWT.create(ContestService.class);
		connectionService = GWT.create(ConnectionService.class);
		eventBus = new HandlerManager(null);
		messageResource = new MessageResource();
		StandardValidationMessages standardValidationMessages = GWT.create(StandardValidationMessagesImpl.class);
	    validationMessages = new CustomValidationMessages(standardValidationMessages);
		iconResource = GWT.create(IconResource.class);
		notificationPopup = new NotificationPopup(eventBus);
//		refreshNavigationPages();
	}

	public ContestServiceAsync getContestService() {
		return contestService;
	}

	public ConnectionServiceAsync getConnectionService() {
		return connectionService;
	}

	public static synchronized LocalStorage getInstance() {
		if (localStorage == null) {
			localStorage = new LocalStorage();
		}
		return localStorage;
	}
	
	public MessageResource getMessageResource() {
		return messageResource;
	}
	
	public List<Object> getNavigationPages() {
		//TODO there is a chance that first time LocalStorage is instantiated call to retrieve navigationPages has not finished yet. think of a way to avoid this.
		//implement some wait period, synchronization or something
		return navigationPages;
	}
	
	public IconResource getIconResource() {
		return iconResource;
	}

	public Presenter getPresenter(String name) {
		logger.log(Level.FINE, "getPresenter: " + name);
		//make first letter lower case. this is a bit of a hack because we need to keep page kind type names lower case for consistency with other enums,
		//while we want upper case page names, but they have to match
		name = Character.toLowerCase(name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
		Presenter result = null;
		try {
			// TODO redesign this if maybe
			if (PresenterName.addComment.name().equals(name)) {
				name = PresenterName.editComment.name();
			} else if (PresenterName.addPage.name().equals(name)) {
				name = PresenterName.editPage.name();
			} else if (PresenterName.addList.name().equals(name)) {
				name = PresenterName.editList.name();
			} else if (PresenterName.addContest.name().equals(name)) {
				name = PresenterName.editContest.name();
			} else if (PresenterName.addText.name().equals(name)) {
				name = PresenterName.editText.name();
			} else if (PresenterName.addNews.name().equals(name)) {
				name = PresenterName.editNews.name();
			} else if (PresenterName.addForumPost.name().equals(name)) {
				name = PresenterName.editForumPost.name();
			} else if (PresenterName.addEpisode.name().equals(name)) {
				name = PresenterName.editEpisode.name();
			} else if (PresenterName.addSerial.name().equals(name)) {
				name = PresenterName.editSerial.name();
			} else if (PresenterName.addConnection.name().equals(name)) {
				name = PresenterName.editConnection.name();
			}
			PresenterName presenterName = Enum.valueOf(PresenterName.class, name);
			result = presenters.get(presenterName);
			if (result == null) {
				switch (presenterName) {
				case page:
					PageView pageView = new PageViewImpl();
					result = new PagePresenter(pageService, contentService, eventBus, pageView);
					break;
				case addPage:
				case editPage:
					EditPageView editPageView = new EditPageViewImpl();
					result = new EditPagePresenter(pageService, eventBus, editPageView);
					break;
				case content:
					ContentView contentViewImpl = new ContentViewImpl();
					result = new ContentPresenter(contentService, eventBus, contentViewImpl);
					break;
				case addComment:
				case editComment:
					EditCommentView editCommentView = new EditCommentViewImpl();
					result = new EditCommentPresenter(contentService, eventBus, editCommentView);
					break;
				case activate:
					ActivateUserProfileView activateUserProfileView = new ActivateUserProfileViewImpl();
					result = new ActivateUserProfilePresenter(userProfileService, eventBus, activateUserProfileView);
					break;
				case addUser:
					AddUserProfileView addUserProfileView = new AddUserProfileViewImpl();
					result = new AddUserProfilePresenter(userProfileService, eventBus, addUserProfileView);
					break;
				case editUser:
					EditUserProfileView editUserProfileView = new EditUserProfileViewImpl();
					result = new EditUserProfilePresenter(userProfileService, eventBus, editUserProfileView);
					break;
				case user:
					UserProfileView userProfileView = new UserProfileViewImpl();
					result = new UserProfilePresenter(userProfileService, eventBus, userProfileView);
					break;
				case home:
					HomeView homeView = new HomeViewImpl();
					result = new HomePresenter(itemListService, eventBus, homeView);
					break;
				case login:
					LoginView loginView = new LoginViewImpl();
					result = new LoginPresenter(userProfileService, eventBus, loginView);
					break;
				case status:
					StatusView statusView = new StatusViewImpl();
					result = new StatusPresenter(eventBus, statusView);
					break;
				case lists:
					ItemListView itemListViewImpl = new ItemListViewImpl();
					result = new ItemListPresenter(itemListService, contentService, userProfileService, contestService, connectionService, eventBus, itemListViewImpl);
					break;
				case addList:
				case editList:
					EditItemListView editItemListView = new EditItemListViewImpl();
					result = new EditItemListPresenter(itemListService, eventBus, editItemListView);
					break;
				case forum:
					ForumView forumView = new ForumViewImpl();
					result = new ForumPresenter(contentService, itemListService, eventBus, forumView);
					break;
				case addForumPost:
				case editForumPost:
					EditForumPostView editForumPostView = new EditForumPostViewImpl();
					result = new EditForumPostPresenter(contentService, eventBus, editForumPostView);
					break;
				case serials:
					SerialView serialView = new SerialViewImpl();
					result = new SerialPresenter(itemListService, contentService, eventBus, serialView);
					break;
				case addSerial:
				case editSerial:
					EditSerialView editSerialView = new EditSerialViewImpl();
					result = new EditSerialPresenter(contentService, eventBus, editSerialView);
					break;
				case addEpisode:
				case editEpisode:
					EditEpisodeView editEpisodeView = new EditEpisodeViewImpl();
					result = new EditEpisodePresenter(contentService, eventBus, editEpisodeView);
					break;
				case authors:
					AuthorView authorView = new AuthorViewImpl();
					result = new AuthorPresenter(itemListService, eventBus, authorView);
					break;
				case texts:
					TextView textView = new TextViewImpl();
					result = new TextPresenter(contentService, itemListService, eventBus, textView);
					break;
				case addText:
				case editText:
					EditTextView editTextView = new EditTextViewImpl();
					result = new EditTextPresenter(contentService, eventBus, editTextView);
					break;
				case news:
					NewsView newsView = new NewsViewImpl();
					result = new NewsPresenter(contentService, itemListService, eventBus, newsView);
					break;					
				case addNews:
				case editNews:
					EditNewsView editNewsView = new EditNewsViewImpl();
					result = new EditNewsPresenter(contentService, eventBus, editNewsView);
					break;
				case contests:
					ContestView contestView = new ContestViewImpl();
					result = new ContestPresenter(contestService, itemListService, eventBus, contestView);
					break;
				case addContest:
				case editContest:
					EditContestView editContestView = new EditContestViewImpl();
					result = new EditContestPresenter(contestService, eventBus, editContestView);
					break;
				case connections:
					ConnectionView connectionView = new ConnectionViewImpl();
					result = new ConnectionPresenter(connectionService, itemListService, eventBus, connectionView);
					break;
				case addConnection:
				case editConnection:
					EditConnectionView editConnectionView = new EditConnectionViewImpl();
					result = new EditConnectionPresenter(connectionService, eventBus, editConnectionView);
					break;
				case addMessage:
					EditMessageView editMessageView = new EditMessageViewImpl();
					result = new EditMessagePresenter(userProfileService, eventBus, editMessageView);
					break;
				case sidebar:
					SidebarView sidebarView = new SidebarViewImpl();
					result = new SidebarPresenter(itemListService, eventBus, sidebarView);
					break;
				case editLabels:
					EditLabelsView editLabelsView = new EditLabelsViewImpl();
					result = new EditLabelsPresenter(contentService, eventBus, editLabelsView);
					break;
				}
				logger.log(Level.FINE, "LocalStorage.getPresenter() created " + name);
				presenters.put(presenterName, result);
			}
		} catch (IllegalArgumentException iaex) {

		}
		return result;
	}
	
	public void refreshNavigationPages() {
		pageService.findNavigationPages(new AsyncCallback<List<Object>>() {
			public void onSuccess(List<Object> result) {
				navigationPages = result;
				eventBus.fireEvent(new NavigationPagesLoadedEvent(navigationPages));
			}		      
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error fetching navigation pages", caught);
			}
		});
	}

	public ContentServiceAsync getContentService() {
		return contentService;
	}

	public PageServiceAsync getPageService() {
		return pageService;
	}

	public UserProfileServiceAsync getUserProfileService() {
		return userProfileService;
	}

	public ItemListServiceAsync getItemListService() {
		return itemListService;
	}

	public NotificationPopup getNotificationPopup() {
		return notificationPopup;
	}

	public CustomValidationMessages getValidationMessages() {
		return validationMessages;
	}

	public DateTimeFormat getDateTimeformat() {
		return dateTimeFormat;
	}

	public HashSet<Long> getRatedContentIds() {
		return ratedContentIds;
	}
	
	public static Navigation getNavigation(int width, int height) {
		if (navigation == null) {
			logger.log(Level.SEVERE, "creating navigation: " + width + "x" + height);
			navigation = new Navigation(width, height);
		}
		return navigation;
	}
	
	public static Navigation getNavigation() {
		if (navigation == null) {
			//TODO determine size for navigation dynamically
			getNavigation(800, 200);
		}
		return navigation;
	}

	public String getUsername() {
		String result = null;
		LoginPresenter loginPresenter = (LoginPresenter)getPresenter(PresenterName.login.name());
		if ((loginPresenter != null) && (loginPresenter.getUserDTO() != null)) {
			result = loginPresenter.getUserDTO().getUsername();
		}
		return result;
	}
	
	public ItemListDTO getItemListDTO(final ItemListNameType itemListName, String... params) {
		logger.log(Level.FINEST, "itemListNameType: " + itemListName);
		ItemListDTO result = itemLists.get(itemListName);
		if (result == null) {
			logger.log(Level.FINEST, "itemListService: " + itemListName);
			itemListService.findItemListByName(itemListName.getItemListName(), null, null, new AsyncCallback<ItemListDTO>() {
				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, "Error getting list: ", caught);
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
				}
				@Override
				public void onSuccess(ItemListDTO itemListDTO) {
					logger.log(Level.FINE, "onSuccess: " + itemListName);
					if (itemListDTO != null) {
						itemLists.put(itemListName, itemListDTO);
						eventBus.fireEvent(new ItemListRefreshedEvent(itemListName));
					} else {
						itemLists.put(itemListName, new ItemListDTO());
					}
				}}
			);
		} else if (result.getQuery() == null) {
			result = null;
		} else {
			//this is a bit hacky, we want lists in a map to be generic, but the method should return query with params inserted
			//we have to clone map element and replace them every time 
			result = new ItemListDTO(result);
			if (Utils.hasText(result.getQuery()) && Utils.hasElements(params)) {
				String modifiedQuery = result.getQuery();
				for (int i = 0; i < params.length; i++) {
					String param = params[i];
					modifiedQuery = modifiedQuery.replace("%" + i + "%", param);
				}
				result.setQuery(modifiedQuery);
			}
		}
		logger.log(Level.FINE, "result: " + result);
		return result;
	}
	
	public HandlerManager getEventBus() {
		return eventBus;
	}

	public boolean isUserLoggedIn() {
		LoginPresenter loginPresenter = (LoginPresenter)getPresenter(PresenterName.login.name());
		return ((loginPresenter != null) && (loginPresenter.getUserDTO() != null));
	}
	
	public boolean isUserModerator() {
		//TODO this is going to be changed once rating is fully implemented
		LoginPresenter loginPresenter = (LoginPresenter)getPresenter(PresenterName.login.name());
		return (loginPresenter != null) && 
				(loginPresenter.getUserDTO() != null) && 
				(loginPresenter.getUserDTO().getDescriptionContent() != null) && 
				(loginPresenter.getUserDTO().getDescriptionContent().getRating() != null) &&
				(loginPresenter.getUserDTO().getDescriptionContent().getRating().getValue() < 0);
	}
	
	public boolean isUserAdmin() {
		LoginPresenter loginPresenter = (LoginPresenter)getPresenter(PresenterName.login.name());
		return (loginPresenter != null) && 
				(loginPresenter.getUserDTO() != null) && 
				(loginPresenter.getUserDTO().getDescriptionContent() != null) && 
				(loginPresenter.getUserDTO().getDescriptionContent().getRating() != null) &&
				(loginPresenter.getUserDTO().getDescriptionContent().getRating().getValue() < -99);
	}
}
