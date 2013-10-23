package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.serial.AddEpisodeEvent;
import org.bundolo.client.event.serial.AddSerialEvent;
import org.bundolo.client.event.serial.EditEpisodeEvent;
import org.bundolo.client.event.serial.EditSerialEvent;
import org.bundolo.client.event.serial.EpisodeUpdatedEvent;
import org.bundolo.client.event.serial.EpisodeUpdatedEventHandler;
import org.bundolo.client.event.serial.SerialUpdatedEvent;
import org.bundolo.client.event.serial.SerialUpdatedEventHandler;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.client.view.serial.SerialView;
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

public class SerialPresenter implements Presenter, SerialView.Presenter {
	
	private static final Logger logger = Logger.getLogger(SerialPresenter.class.getName());

	private final HandlerManager eventBus;
	private final SerialView view;
	private ItemListDTO serialsItemListDTO;
	private ItemListDTO serialEpisodesItemListDTO;
	private ItemListPresenter serialEpisodeListPresenter;
	private ContentDTO serialContentDTO;
	private ContentDTO episodeContentDTO;
	private final ItemListServiceAsync itemListService;
	private final ContentServiceAsync contentService;

	public SerialPresenter(ItemListServiceAsync itemListService, ContentServiceAsync contentService, HandlerManager eventBus, SerialView view) {
		this.itemListService = itemListService;
		this.contentService = contentService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ItemListView serialEpisodeItemListViewImpl = new ItemListViewImpl();
		serialEpisodeListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, serialEpisodeItemListViewImpl);
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding view");
		container.add(view.asWidget());
		
		itemListService.findItemListByName(ItemListNameType.serials.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				serialsItemListDTO = result;
				view.displaySerials();
			}}
		);
	}
	
	private void doSerialUpdated(ContentDTO contentDTO) {		
		setSerialContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.serials);
	}
	
	private void doEpisodeUpdated(ContentDTO contentDTO) {
		Utils.setHistoryTokenIfNeeded(PresenterName.serials);
		loadSerialEpisodes();
		setEpisodeContentDTO(contentDTO);
	}

	private void bind() {
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.episode_comment.equals(event.getUpdatedContent().getKind())) {
					view.displayEpisode();
					Utils.setHistoryTokenIfNeeded(PresenterName.serials);
				} else if (ContentKindType.episode_group_comment.equals(event.getUpdatedContent().getKind())) {
					view.displaySerialEpisodes();
					Utils.setHistoryTokenIfNeeded(PresenterName.serials);
				}
			}
		});
		eventBus.addHandler(SerialUpdatedEvent.TYPE, new SerialUpdatedEventHandler() {
			public void onSerialUpdated(SerialUpdatedEvent event) {
				doSerialUpdated(event.getUpdatedContent());
			}
		});
		eventBus.addHandler(EpisodeUpdatedEvent.TYPE, new EpisodeUpdatedEventHandler() {
			public void onEpisodeUpdated(EpisodeUpdatedEvent event) {
				doEpisodeUpdated(event.getUpdatedContent());
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public ItemListDTO getSerialsItemListDTO() {
		return serialsItemListDTO;
	}

	@Override
	public ItemListDTO getSerialEpisodesItemListDTO() {
		return serialEpisodesItemListDTO;
	}

	@Override
	public void setSerialContentDTO(ContentDTO contentDTO) {
		this.serialContentDTO = contentDTO;
		loadSerialEpisodes();
	}
	
	private void loadSerialEpisodes() {
		itemListService.findItemListByName(ItemListNameType.serial_episodes.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE, serialContentDTO.getContentId().toString()), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				serialEpisodesItemListDTO = result;
				view.displaySerialEpisodes();
			}}
		);
	}

	@Override
	public void setEpisodeContentDTO(ContentDTO contentDTO) {
		this.episodeContentDTO = contentDTO;
		view.displayEpisode();
	}

	@Override
	public ItemListPresenter getSerialEpisodeListPresenter() {
		return serialEpisodeListPresenter;
	}

	@Override
	public ContentDTO getEpisodeContentDTO() {
		return episodeContentDTO;
	}
	
	private void deleteCurrentSerial() {
		contentService.deleteContent(serialContentDTO.getContentId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting serial: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_deleted)));
			}}
		);
	}
	
	private void deleteCurrentEpisode() {
		contentService.deleteContent(episodeContentDTO.getContentId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting episode: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.episode_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.episode_deleted)));
			}}
		);
	}

	@Override
	public void onAddSerialButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addSerial.name());
		eventBus.fireEvent(new AddSerialEvent());
	}

	@Override
	public void onDeleteSerialButtonClicked() {
		if ((serialContentDTO != null) && (serialContentDTO.getContentId() != null)) {
			deleteCurrentSerial();
		}
	}

	@Override
	public void onEditSerialButtonClicked() {
		if ((serialContentDTO != null) && (serialContentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editSerial.name());
			eventBus.fireEvent(new EditSerialEvent(serialContentDTO));
		}
	}

	@Override
	public void onAddEpisodeButtonClicked() {
		if ((serialContentDTO != null) && (serialContentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.addEpisode.name());
			eventBus.fireEvent(new AddEpisodeEvent(serialContentDTO));
		}
		
	}

	@Override
	public void onDeleteEpisodeButtonClicked() {
		if ((episodeContentDTO != null) && (episodeContentDTO.getContentId() != null)) {
			deleteCurrentEpisode();
		}
	}

	@Override
	public void onEditEpisodeButtonClicked() {
		if ((episodeContentDTO != null) && (episodeContentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editEpisode.name());
			eventBus.fireEvent(new EditEpisodeEvent(episodeContentDTO));
		}
	}

	@Override
	public ContentDTO getSerialContentDTO() {
		return serialContentDTO;
	}
}