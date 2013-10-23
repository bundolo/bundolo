package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.text.AddTextEvent;
import org.bundolo.client.event.text.AddTextEventHandler;
import org.bundolo.client.event.text.EditTextEvent;
import org.bundolo.client.event.text.EditTextEventHandler;
import org.bundolo.client.event.text.TextUpdatedEvent;
import org.bundolo.client.view.content.EditTextView;
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

public class EditTextPresenter implements Presenter, EditTextView.Presenter {

	private static final Logger logger = Logger.getLogger(EditTextPresenter.class.getName());

	private ContentDTO contentDTO;
	private final ContentServiceAsync contentService;
	private final HandlerManager eventBus;
	private final EditTextView view;
	private ValidationProcessor validator;

	public EditTextPresenter(ContentServiceAsync contentService, HandlerManager eventBus, EditTextView view) {
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
	public void onSaveTextButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelTextButtonClicked() {
		doEditTextCancelled();
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
		contentDTO.setName(view.getTitle());
		contentDTO.setText(view.getText());
		//contentDTO.setKind(view.getKind());
		//contentDTO.setContentStatus(view.getContentStatus());
		contentDTO.getDescriptionContent().setText(view.getDescription());
		if (contentDTO.getContentId() == null) {
			contentDTO.setKind(ContentKindType.text);
			contentDTO.setContentStatus(ContentStatusType.active); //TODO we don't use other statuses at the moment
			contentService.saveContent(contentDTO, new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "saveContent onSuccess");
					contentDTO.setContentId(result);
					eventBus.fireEvent(new TextUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_added)));
				}
			});
		} else {
			logger.log(Level.WARNING, "updateContent start");
			contentService.updateContent(contentDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateContent onSuccess");
					eventBus.fireEvent(new TextUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_updated)));
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

	private void doAddText() {		
		ContentDTO newContentDTO = new ContentDTO();
		ContentDTO descriptionContentDTO = new ContentDTO();
		newContentDTO.setDescriptionContent(descriptionContentDTO);
		setContentDTO(newContentDTO);		
		Utils.setHistoryTokenIfNeeded(PresenterName.addText);
	}

	private void doEditText(ContentDTO contentDTO) {
		if (contentDTO.getDescriptionContent() == null) { //just a fix for db inconsistencies, we won't have texts without description
			ContentDTO descriptionContentDTO = new ContentDTO();
			contentDTO.setDescriptionContent(descriptionContentDTO);
		}
		setContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editText);
	}

	private void doEditTextCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddTextEvent.TYPE, new AddTextEventHandler() {
			public void onAddText(AddTextEvent event) {
				doAddText();
			}
		});

		eventBus.addHandler(EditTextEvent.TYPE, new EditTextEventHandler() {
			public void onEditText(EditTextEvent event) {
				doEditText(event.getContentDTO());
			}
		});
	}

	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}