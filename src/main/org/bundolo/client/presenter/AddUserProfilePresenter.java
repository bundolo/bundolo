package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.user.AddUserProfileView;
import org.bundolo.shared.model.UserProfileDTO;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class AddUserProfilePresenter implements Presenter, AddUserProfileView.Presenter {

	private static final Logger logger = Logger.getLogger(AddUserProfilePresenter.class.getName());

	private UserProfileDTO userProfileDTO;
	private final UserProfileServiceAsync userProfileService;
	private final HandlerManager eventBus;
	private final AddUserProfileView view;
	private ValidationProcessor validator;

	public AddUserProfilePresenter(UserProfileServiceAsync userProfileService, HandlerManager eventBus, AddUserProfileView view) {
		this.userProfileService = userProfileService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		userProfileDTO = new UserProfileDTO();
		bind();
	}

	@Override
	public void onSaveUserProfileButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelUserProfileButtonClicked() {
		doEditUserProfileCancelled();
	}

	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "go");
		if (LocalStorage.getInstance().isUserLoggedIn()) {
			History.back();
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.already_logged_in)));
		} else {
			
		}
		container.clear();
		container.add(view.asWidget());
	}

	private void doSave() {
		logger.log(Level.FINE, "doSave start");
		userProfileDTO.setEmail(view.getEmail());
		userProfileDTO.setPassword(view.getPassword());
		userProfileDTO.setUsername(view.getUsername());
		
		userProfileService.saveUserProfile(userProfileDTO, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException) {
	                ValidationException ex = (ValidationException) caught;
	                logger.log(Level.FINE, "Server side validation failed: ", ex);
	                validator.processServerErrors(ex);
	            } else {
	            	logger.log(Level.SEVERE, "Error adding UserProfile: ", caught);
	            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.account_adding_failed)));
	            }
			}
			@Override
			public void onSuccess(Void result) {
				logger.log(Level.FINE, "doSave onSuccess");
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.account_added)));
			}
		});
	}

	private void doEditUserProfileCancelled() {
		History.back();
	}
	
	private void bind() {
	}

	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}

}