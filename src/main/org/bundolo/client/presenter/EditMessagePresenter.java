package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.AddMessageEvent;
import org.bundolo.client.event.user.AddMessageEventHandler;
import org.bundolo.client.event.user.MessageSentEvent;
import org.bundolo.client.view.user.EditMessageView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditMessagePresenter implements Presenter, EditMessageView.Presenter {

    private static final Logger logger = Logger.getLogger(EditMessagePresenter.class.getName());

    private UserDTO recipientUserDTO;

    private final UserProfileServiceAsync userProfileService;
    private final HandlerManager eventBus;
    private final EditMessageView view;
    private final ValidationProcessor validator;

    public EditMessagePresenter(UserProfileServiceAsync userProfileService, HandlerManager eventBus,
	    EditMessageView view) {
	this.userProfileService = userProfileService;
	this.eventBus = eventBus;
	this.view = view;
	validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
	this.view.setPresenter(this);
	bind();
    }

    @Override
    public void onSendMessageButtonClicked() {
	if (validator.validate()) {
	    doSend();
	}
    }

    @Override
    public void onCancelMessageButtonClicked() {
	doEditMessageCancelled();
    }

    @Override
    public void go(final HasWidgets container) {
	logger.log(Level.FINE, "go");
	if (!LocalStorage.getInstance().isUserLoggedIn()) {
	    History.back();
	    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
		    .getLabel(LabelType.no_permission)));
	}
	container.clear();
	container.add(view.asWidget());
    }

    private void doSend() {
	logger.log(Level.FINE, "doSend start");
	// TODO validation
	userProfileService.sendMessage(view.getTitle(), view.getTitle(), recipientUserDTO.getUsername(),
		new AsyncCallback<Boolean>() {
		    @Override
		    public void onFailure(Throwable caught) {
			if (caught instanceof ValidationException) {
			    ValidationException ex = (ValidationException) caught;
			    logger.log(Level.FINE, "Server side validation failed: ", ex);
			    validator.processServerErrors(ex);
			} else {
			    logger.log(Level.SEVERE, "Error sending message: ", caught);
			    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance()
				    .getMessageResource().getLabel(LabelType.message_sending_failed)));
			}
		    }

		    @Override
		    public void onSuccess(Boolean result) {
			logger.log(Level.FINE, "sendMessage onSuccess");
			if (result) {
			    // TODO define what should happen after message
			    // sending
			    eventBus.fireEvent(new MessageSentEvent(recipientUserDTO));
			    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance()
				    .getMessageResource().getLabel(LabelType.message_sent)));
			} else {
			    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance()
				    .getMessageResource().getLabel(LabelType.user_not_found)));
			}
		    }
		});
    }

    private void doAddMessage(UserDTO recipientUserDTO) {
	this.recipientUserDTO = recipientUserDTO;
	Utils.setHistoryTokenIfNeeded(PresenterName.addMessage);
    }

    private void doEditMessageCancelled() {
	History.back();
    }

    private void bind() {
	eventBus.addHandler(AddMessageEvent.TYPE, new AddMessageEventHandler() {
	    @Override
	    public void onAddMessage(AddMessageEvent event) {
		doAddMessage(event.getRecipientUserDTO());
	    }
	});
    }

    @Override
    public ValidationProcessor getValidator() {
	return validator;
    }
}