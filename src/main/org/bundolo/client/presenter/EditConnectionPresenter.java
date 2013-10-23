package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.connection.AddConnectionEvent;
import org.bundolo.client.event.connection.AddConnectionEventHandler;
import org.bundolo.client.event.connection.ConnectionUpdatedEvent;
import org.bundolo.client.event.connection.EditConnectionEvent;
import org.bundolo.client.event.connection.EditConnectionEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.connection.EditConnectionView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ConnectionKindType;
import org.bundolo.shared.model.enumeration.ConnectionStatusType;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ConnectionServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditConnectionPresenter implements Presenter, EditConnectionView.Presenter {

	private static final Logger logger = Logger.getLogger(EditConnectionPresenter.class.getName());

	private ConnectionDTO connectionDTO;
	private final ConnectionServiceAsync connectionService;
	private final HandlerManager eventBus;
	private final EditConnectionView view;
	private ValidationProcessor validator;

	public EditConnectionPresenter(ConnectionServiceAsync connectionService, HandlerManager eventBus, EditConnectionView view) {
		this.connectionService = connectionService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void onSaveConnectionButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelConnectionButtonClicked() {
		doEditConnectionCancelled();
	}

	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "go");
		if (!LocalStorage.getInstance().isUserLoggedIn()) {
			History.back();
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.no_permission)));
		}
		container.clear();
		container.add(view.asWidget());
	}

	private void doSave() {
		logger.log(Level.FINE, "doSave start");

		//TODO
		connectionDTO.setEmail(view.getEmail());
		connectionDTO.setUrl(view.getUrl());
		connectionDTO.getDescriptionContent().setName(view.getName());
		connectionDTO.getDescriptionContent().setText(view.getDescription());	
		if (connectionDTO.getConnectionId() == null) {
			connectionDTO.setConnectionStatus(ConnectionStatusType.active);
			connectionService.saveConnection(connectionDTO, new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving connection: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "saveConnection onSuccess");
					connectionDTO.setConnectionId(result);
					eventBus.fireEvent(new ConnectionUpdatedEvent(connectionDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_added)));
				}
			});
		} else {
			connectionService.updateConnection(connectionDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating connection: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateConnection onSuccess");
					eventBus.fireEvent(new ConnectionUpdatedEvent(connectionDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_updated)));
				}
			});
		}
	}

	@Override
	public ConnectionDTO getConnectionDTO() {
		return connectionDTO;
	}

	@Override
	public void setConnectionDTO(ConnectionDTO connectionDTO) {
		this.connectionDTO = connectionDTO;
	}

	private void doAddConnection(ContentDTO parentContentDTO) {
		ConnectionDTO newConnectionDTO = new ConnectionDTO();
		newConnectionDTO.setKind(ConnectionKindType.general);
		newConnectionDTO.setParentContentId(parentContentDTO.getContentId());
		ContentDTO newConnectionDescriptionContent = new ContentDTO();
		newConnectionDescriptionContent.setKind(ContentKindType.connection_description);
		newConnectionDescriptionContent.setContentStatus(ContentStatusType.active);
		newConnectionDTO.setDescriptionContent(newConnectionDescriptionContent);
		setConnectionDTO(newConnectionDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addConnection);
	}

	private void doEditConnection(ConnectionDTO connectionDTO) {
		setConnectionDTO(connectionDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editConnection);
	}

	private void doEditConnectionCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddConnectionEvent.TYPE, new AddConnectionEventHandler() {
			public void onAddConnection(AddConnectionEvent event) {
				doAddConnection(event.getParentContentDTO());
			}
		});

		eventBus.addHandler(EditConnectionEvent.TYPE, new EditConnectionEventHandler() {
			public void onEditConnection(EditConnectionEvent event) {
				doEditConnection(event.getConnectionDTO());
			}
		});
	}
	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}