package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.event.list.ItemListRefreshedEvent;
import org.bundolo.client.event.list.ItemListRefreshedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.text.TextUpdatedEvent;
import org.bundolo.client.event.text.TextUpdatedEventHandler;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.client.view.page.PageView;
import org.bundolo.client.view.page.PageViewImpl;
import org.bundolo.client.view.sidebar.SidebarView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class SidebarPresenter implements Presenter, SidebarView.Presenter {
	
	private static final Logger logger = Logger.getLogger(SidebarPresenter.class.getName());

	private final HandlerManager eventBus;
	private final SidebarView view;
	//private HasWidgets container;
	private ItemListDTO newTextsItemListDTO;
	private ItemListDTO authorsItemListDTO;
	private ItemListDTO newsItemListDTO;
	private ItemListDTO newContestsItemListDTO;
	private ItemListDTO newConnectionsItemListDTO;
	private ItemListDTO newForumPostsItemListDTO;
	private ItemListDTO newEpisodesItemListDTO;
	private final ItemListServiceAsync itemListService;
	private ItemListPresenter newTextsListPresenter;
	private ItemListPresenter authorsListPresenter;
	private ItemListPresenter newsListPresenter;
	private ItemListPresenter newContestsListPresenter;
	private ItemListPresenter newConnectionsListPresenter;
	private ItemListPresenter newForumPostsListPresenter;
	private ItemListPresenter newEpisodesListPresenter;

	public SidebarPresenter(ItemListServiceAsync itemListService, HandlerManager eventBus, SidebarView view) {
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		ItemListView newTextsItemListViewImpl = new ItemListViewImpl();
		newTextsListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newTextsItemListViewImpl);
		ItemListView authorsItemListViewImpl = new ItemListViewImpl();
		authorsListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, authorsItemListViewImpl);
		ItemListView newsItemListViewImpl = new ItemListViewImpl();
		newsListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newsItemListViewImpl);
		ItemListView newContestsItemListViewImpl = new ItemListViewImpl();
		newContestsListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newContestsItemListViewImpl);
		ItemListView newConnectionsItemListViewImpl = new ItemListViewImpl();
		newConnectionsListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newConnectionsItemListViewImpl);
		ItemListView newForumPostsItemListViewImpl = new ItemListViewImpl();
		newForumPostsListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newForumPostsItemListViewImpl);
		ItemListView newEpisodesItemListViewImpl = new ItemListViewImpl();
		newEpisodesListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newEpisodesItemListViewImpl);
		bind();
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding view");
		container.add(view.asWidget());
		storeListAndDisplay(ItemListNameType.new_texts);
		storeListAndDisplay(ItemListNameType.authors);
		storeListAndDisplay(ItemListNameType.latest_news);
		storeListAndDisplay(ItemListNameType.new_contests);
		storeListAndDisplay(ItemListNameType.new_connections);
		storeListAndDisplay(ItemListNameType.new_forum_posts);
		storeListAndDisplay(ItemListNameType.serials);
	}

	private void bind() {
		eventBus.addHandler(ItemListRefreshedEvent.TYPE, new ItemListRefreshedEventHandler() {
			@Override
			public void onItemListRefreshed(ItemListRefreshedEvent event) {
				storeListAndDisplay(event.getRefreshedItemListName());
			}
		});
	}
		
	private void storeListAndDisplay(ItemListNameType itemListName) {
		logger.log(Level.FINEST, "itemListName: " + itemListName);
		
		switch (itemListName) {
		case new_texts:
			newTextsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.new_texts, Constants.DEFAULT_LOCALE);
			if (newTextsItemListDTO != null) {
				view.displayNewTexts();
			}
			break;
		case authors:
			authorsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.authors);
			if (authorsItemListDTO != null) {
				view.displayAuthors();
			}
			break;
		case latest_news:
			newsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.latest_news, Constants.DEFAULT_LOCALE);
			if (newsItemListDTO != null) {
				view.displayNews();
			}
			break;
		case new_contests:
			newContestsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.new_contests, Constants.DEFAULT_LOCALE);
			if (newContestsItemListDTO != null) {
				view.displayNewContests();
			}
			break;
		case new_connections:
			newConnectionsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.new_connections, Constants.DEFAULT_LOCALE);
			if (newConnectionsItemListDTO != null) {
				view.displayNewConnections();
			}
			break;
		case new_forum_posts:
			newForumPostsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.new_forum_posts, Constants.DEFAULT_LOCALE);
			if (newForumPostsItemListDTO != null) {
				view.displayNewForumPosts();
			}
			break;
		case serials:
			newEpisodesItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.serials, Constants.DEFAULT_LOCALE);
			if (newEpisodesItemListDTO != null) {
				view.displayNewEpisodes();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public ItemListDTO getNewTextsItemListDTO() {
		return newTextsItemListDTO;
	}
	
	@Override
	public ItemListDTO getAuthorsItemListDTO() {
		return authorsItemListDTO;
	}

	@Override
	public ItemListPresenter getNewTextsListPresenter() {
		return newTextsListPresenter;
	}

	@Override
	public ItemListPresenter getAuthorsListPresenter() {
		return authorsListPresenter;
	}

	@Override
	public ItemListDTO getNewsItemListDTO() {
		return newsItemListDTO;
	}

	@Override
	public ItemListDTO getNewForumPostsItemListDTO() {
		return newForumPostsItemListDTO;
	}

	@Override
	public ItemListDTO getNewContestsItemListDTO() {
		return newContestsItemListDTO;
	}

	@Override
	public ItemListDTO getNewConnectionsItemListDTO() {
		return newConnectionsItemListDTO;
	}

	@Override
	public ItemListDTO getNewEpisodesItemListDTO() {
		return newEpisodesItemListDTO;
	}

	@Override
	public ItemListPresenter getNewsListPresenter() {
		return newsListPresenter;
	}

	@Override
	public ItemListPresenter getNewForumPostsListPresenter() {
		return newForumPostsListPresenter;
	}

	@Override
	public ItemListPresenter getNewContestsListPresenter() {
		return newContestsListPresenter;
	}

	@Override
	public ItemListPresenter getNewConnectionsListPresenter() {
		return newConnectionsListPresenter;
	}

	@Override
	public ItemListPresenter getNewEpisodesListPresenter() {
		return newEpisodesListPresenter;
	}

}
