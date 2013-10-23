package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.serial.AddSerialEvent;
import org.bundolo.client.event.serial.AddSerialEventHandler;
import org.bundolo.client.event.serial.EditSerialEvent;
import org.bundolo.client.event.serial.EditSerialEventHandler;
import org.bundolo.client.event.serial.SerialUpdatedEvent;
import org.bundolo.client.view.serial.EditSerialView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContentServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditSerialPresenter implements Presenter, EditSerialView.Presenter {

	private static final Logger logger = Logger.getLogger(EditSerialPresenter.class.getName());

	private ContentDTO contentDTO;
	private final ContentServiceAsync contentService;
	private final HandlerManager eventBus;
	private final EditSerialView view;
	private ValidationProcessor validator;

	public EditSerialPresenter(ContentServiceAsync contentService, HandlerManager eventBus, EditSerialView view) {
		this.contentService = contentService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		
		//TODO instead of this, we are supposed to set real content
		contentDTO = new ContentDTO();
		bind();
	}

	@Override
	public void onSaveSerialButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelSerialButtonClicked() {
		doEditSerialCancelled();
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

		contentDTO.setName(view.getName());
		contentDTO.setText(view.getText());		
		if (contentDTO.getContentId() == null) {
			contentDTO.setContentStatus(ContentStatusType.active); //TODO we don't use other statuses at the moment
			contentService.saveContent(contentDTO, new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "saveContent onSuccess");
					contentDTO.setContentId(result);
					eventBus.fireEvent(new SerialUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_added)));
				}
			});
		} else {
			contentService.updateContent(contentDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateContent onSuccess");
					eventBus.fireEvent(new SerialUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_updated)));
				}
			});
		}
		
	}

	@Override
	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public void setContentDTO(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	private void doAddSerial() {		
		ContentDTO newContentDTO = new ContentDTO();
		newContentDTO.setKind(ContentKindType.episode_group);
		//newContentDTO.setParentPageId(contentDTO.getParentPageId());
		setContentDTO(newContentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addSerial);
	}

	private void doEditSerial(ContentDTO contentDTO) {
		setContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editSerial);
	}

	private void doEditSerialCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddSerialEvent.TYPE, new AddSerialEventHandler() {
			public void onAddSerial(AddSerialEvent event) {
				doAddSerial();
			}
		});

		eventBus.addHandler(EditSerialEvent.TYPE, new EditSerialEventHandler() {
			public void onEditSerial(EditSerialEvent event) {
				doEditSerial(event.getContentDTO());
			}
		});
	}
	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}