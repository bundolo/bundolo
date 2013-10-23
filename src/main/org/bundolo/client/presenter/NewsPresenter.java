package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.news.AddNewsEvent;
import org.bundolo.client.event.news.EditNewsEvent;
import org.bundolo.client.event.news.NewsUpdatedEvent;
import org.bundolo.client.event.news.NewsUpdatedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.content.ContentViewImpl;
import org.bundolo.client.view.news.NewsView;
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

public class NewsPresenter implements Presenter, NewsView.Presenter {
	
	private static final Logger logger = Logger.getLogger(NewsPresenter.class.getName());

	private final HandlerManager eventBus;
	private final NewsView view;
	private ItemListDTO newsItemListDTO;
	private ContentDTO singleNewsContentDTO;
	private final ContentServiceAsync contentService;
	private final ItemListServiceAsync itemListService;
	private ContentPresenter newsContentPresenter;

	public NewsPresenter(ContentServiceAsync contentService, ItemListServiceAsync itemListService, HandlerManager eventBus, NewsView view) {
		this.contentService = contentService;
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ContentViewImpl contentViewImpl = new ContentViewImpl();
		newsContentPresenter = new ContentPresenter(LocalStorage.getInstance().getContentService(), eventBus, contentViewImpl);
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding NewsView");
		container.add(view.asWidget());
		
		itemListService.findItemListByName(ItemListNameType.latest_news.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				newsItemListDTO = result;
				view.displayNews();
			}}
		);
	}
	
	private void doNewsUpdated(ContentDTO contentDTO) {
		setNewsContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.news);
	}

	private void bind() {
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.news_comment.equals(event.getUpdatedContent().getKind())) {
					view.displaySingleNews();
					Utils.setHistoryTokenIfNeeded(PresenterName.news);
				}
			}
		});
		eventBus.addHandler(NewsUpdatedEvent.TYPE, new NewsUpdatedEventHandler() {
			public void onNewsUpdated(NewsUpdatedEvent event) {
				doNewsUpdated(event.getUpdatedContent());
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public ItemListDTO getNewsItemListDTO() {
		return newsItemListDTO;
	}

	@Override
	public void setNewsContentDTO(ContentDTO contentDTO) {
		logger.log(Level.FINE, "setNewsContentDTO: " + contentDTO.getName());
		this.singleNewsContentDTO = contentDTO;
		view.displaySingleNews();
	}

	@Override
	public ContentDTO getNewsContentDTO() {
		return singleNewsContentDTO;
	}

	@Override
	public ContentPresenter getNewsContentPresenter() {
		return newsContentPresenter;
	}
	
	private void deleteCurrentNews() {
		contentService.deleteContent(singleNewsContentDTO.getContentId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting news: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_deleted)));
			}}
		);
	}

	@Override
	public void onAddNewsButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addNews.name());
		eventBus.fireEvent(new AddNewsEvent());
	}

	@Override
	public void onDeleteNewsButtonClicked() {
		if ((singleNewsContentDTO != null) && (singleNewsContentDTO.getContentId() != null)) {
			deleteCurrentNews();
		}
	}

	@Override
	public void onEditNewsButtonClicked() {
		if ((singleNewsContentDTO != null) && (singleNewsContentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editNews.name());
			eventBus.fireEvent(new EditNewsEvent(singleNewsContentDTO));
		}
	}

}
