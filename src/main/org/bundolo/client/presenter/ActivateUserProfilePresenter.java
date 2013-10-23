package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.user.ActivateUserProfileView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class ActivateUserProfilePresenter implements Presenter, ActivateUserProfileView.Presenter {

	private static final Logger logger = Logger.getLogger(ActivateUserProfilePresenter.class.getName());

	private final UserProfileServiceAsync userProfileService;
	private final HandlerManager eventBus;
	private final ActivateUserProfileView view;
	private ValidationProcessor validator;

	public ActivateUserProfilePresenter(UserProfileServiceAsync userProfileService, HandlerManager eventBus, ActivateUserProfileView view) {
		this.userProfileService = userProfileService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		
		bind();
	}

	@Override
	public void onActivateUserProfileButtonClicked() {
		if (validator.validate()) {
			doActivate(view.getActivationEmail(), view.getActivationCode());
		}
	}

	@Override
	public void onCancelActivateUserProfileButtonClicked() {
		doActivateCancelled();
	}

	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "go");
		if (LocalStorage.getInstance().isUserLoggedIn()) {
			History.back();
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.already_logged_in)));
		}
		container.clear();
		container.add(view.asWidget());
		
		String email = Window.Location.getParameter("email");
		String nonce = Window.Location.getParameter("nonce");
		
		if (Utils.hasText(email) && Utils.hasText(nonce)) {
			doActivate(email, nonce);
		}
	}

	private void doActivate(String email, String nonce) {
		logger.log(Level.FINE, "doActivate start");
		
		//TODO display some "waiting for..." message
		userProfileService.activateUserProfileEmailAddress(email, nonce, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException) {
	                ValidationException ex = (ValidationException) caught;
	                logger.log(Level.FINE, "Server side validation failed: ", ex);
	                validator.processServerErrors(ex);
	            } else {
	            	logger.log(Level.SEVERE, "Error activating UserProfile email: ", caught);
	            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_verification_failed)));
	            }
			}
			@Override
			public void onSuccess(Boolean result) {
				logger.log(Level.FINE, "activateUserProfileEmailAddress onSuccess");
				if (result) {
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_verified)));
				} else {
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_verification_failed)));
				}
			}
		});
	}

	private void doActivateCancelled() {
		History.back();
	}
	
	private void bind() {
	}
	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}
