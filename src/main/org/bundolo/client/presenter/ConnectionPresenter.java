package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.connection.AddConnectionEvent;
import org.bundolo.client.event.connection.ConnectionUpdatedEvent;
import org.bundolo.client.event.connection.ConnectionUpdatedEventHandler;
import org.bundolo.client.event.connection.EditConnectionEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.connection.ConnectionView;
import org.bundolo.client.view.content.ContentViewImpl;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ConnectionServiceAsync;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class ConnectionPresenter implements Presenter, ConnectionView.Presenter {
	
	private static final Logger logger = Logger.getLogger(ConnectionPresenter.class.getName());

	private final HandlerManager eventBus;
	private final ConnectionView view;
	private ItemListDTO connectionGroupsItemListDTO;
	private ItemListDTO connectionsItemListDTO;
	private ConnectionDTO connectionDTO;
	private ContentDTO connectionGroupContentDTO;
	private ConnectionServiceAsync connectionService;
	private final ItemListServiceAsync itemListService;
	private ContentPresenter connectionContentPresenter;
	private ItemListPresenter connectionGroupConnectionListPresenter;

	public ConnectionPresenter(ConnectionServiceAsync connectionService, ItemListServiceAsync itemListService, HandlerManager eventBus, ConnectionView view) {
		this.connectionService = connectionService;
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ContentViewImpl contentViewImpl = new ContentViewImpl();
		connectionContentPresenter = new ContentPresenter(LocalStorage.getInstance().getContentService(), eventBus, contentViewImpl);		
		ItemListView connectionGroupConnectionItemListViewImpl = new ItemListViewImpl();
		connectionGroupConnectionListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, connectionGroupConnectionItemListViewImpl);
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding ConnectionView");
		container.add(view.asWidget());
		itemListService.findItemListByName(ItemListNameType.connection_groups.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				connectionGroupsItemListDTO = result;
				view.displayConnectionGroups();
			}}
		);
	}
	
	private void doConnectionUpdated(ConnectionDTO connectionDTO) {
		setConnectionDTO(connectionDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.connections);
	}

	private void bind() {
		eventBus.addHandler(ConnectionUpdatedEvent.TYPE, new ConnectionUpdatedEventHandler() {
			public void onConnectionUpdated(ConnectionUpdatedEvent event) {
				doConnectionUpdated(event.getUpdatedConnection());
			}
		});
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.connection_comment.equals(event.getUpdatedContent().getKind())) {
					view.displayConnection();
					Utils.setHistoryTokenIfNeeded(PresenterName.connections);
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
	public ItemListDTO getConnectionGroupConnectionsItemListDTO() {
		return connectionsItemListDTO;
	}
	
	@Override
	public ItemListDTO getConnectionGroupsItemListDTO() {
		return connectionGroupsItemListDTO;
	}

	@Override
	public void setConnectionDTO(ConnectionDTO connectionDTO) {
		logger.log(Level.FINE, "setConnectionDTO: " + connectionDTO.getDescriptionContent().getName());
		this.connectionDTO = connectionDTO;
		view.displayConnection();
	}
	
	@Override
	public void setConnectionGroupContentDTO(ContentDTO contentDTO) {
		logger.log(Level.FINE, "setConnectionGroupContentDTO: " + contentDTO.getName());
		this.connectionGroupContentDTO = contentDTO;
		reset(0);
		//TODO we need to pass locale but not as a query parameter. we need it in the service, inside loop retrieving connection descriptions. some redesign needed
		itemListService.findItemListByName(ItemListNameType.connection_group_entries.getItemListName(), null, Arrays.asList(contentDTO.getContentId().toString()), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				connectionsItemListDTO = result;
				view.displayConnectionGroupConnections();
			}}
		);
	}

	@Override
	public ConnectionDTO getConnectionDTO() {
		return connectionDTO;
	}

	@Override
	public ContentPresenter getConnectionContentPresenter() {
		return connectionContentPresenter;
	}

	@Override
	public ItemListPresenter getConnectionGroupConnectionListPresenter() {
		return connectionGroupConnectionListPresenter;
	}

	@Override
	public void onAddConnectionButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addConnection.name());
		eventBus.fireEvent(new AddConnectionEvent(connectionGroupContentDTO));
	}

	@Override
	public void onDeleteConnectionButtonClicked() {
		if ((connectionDTO != null) && (connectionDTO.getConnectionId() != null)) {
			deleteCurrentConnection();
		}
	}

	@Override
	public void onEditConnectionButtonClicked() {
		if ((connectionDTO != null) && (connectionDTO.getConnectionId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editConnection.name());
			eventBus.fireEvent(new EditConnectionEvent(connectionDTO));
		}
	}
	
	private void deleteCurrentConnection() {
		connectionService.deleteConnection(connectionDTO.getConnectionId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting connection: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_deleted)));
			}}
		);
	}

	@Override
	public void reset(int level) {
		if (level > 1) {
			connectionGroupContentDTO = null;
		}
		if (level > 0) {
			connectionDTO = null;
		}
		view.reset(level);
	}
}