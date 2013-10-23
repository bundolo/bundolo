package org.bundolo.client.presenter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.text.TextUpdatedEvent;
import org.bundolo.client.event.user.UserProfileUpdatedEvent;
import org.bundolo.client.view.user.EditUserProfileView;
import org.bundolo.server.SessionUtils;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.UserProfileDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditUserProfilePresenter implements Presenter, EditUserProfileView.Presenter {

	private static final Logger logger = Logger.getLogger(EditUserProfilePresenter.class.getName());

	private UserDTO userDTO;
	private final UserProfileServiceAsync userProfileService;
	private final HandlerManager eventBus;
	private final EditUserProfileView view;
	private ValidationProcessor validator;

	public EditUserProfilePresenter(UserProfileServiceAsync userProfileService, HandlerManager eventBus, EditUserProfileView view) {
		this.userProfileService = userProfileService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
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
		LoginPresenter loginPresenter = (LoginPresenter)LocalStorage.getInstance().getPresenter(PresenterName.login.name());
		if ((loginPresenter != null) && (loginPresenter.getUserDTO() != null)) {
			userDTO = loginPresenter.getUserDTO();
		} else {
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.no_permission)));
		}
		container.clear();
		container.add(view.asWidget());
	}

	private void doSave() {
		logger.log(Level.FINE, "doSave start");
		UserProfileDTO userProfileDTO = new UserProfileDTO();
		userProfileDTO.setAvatarUrl(view.getAvatarUrl());
		userProfileDTO.setUsername(view.getUsername());
		userProfileDTO.setBirthDate(view.getBirthDate());
		userProfileDTO.setFirstName(view.getFirstName());
		userProfileDTO.setGender(view.getGender());
		userProfileDTO.setLastName(view.getLastName());
		userProfileDTO.setShowPersonal(view.getShowPersonal());
		userProfileDTO.setSignupDate(userDTO.getSignupDate());
		userProfileDTO.setLastLoginDate(userDTO.getLastLoginDate());
		userProfileDTO.setSessionId(userDTO.getSessionId());
		if (userDTO.getDescriptionContent() == null) { //this won't be used since we have description for each user
			ContentDTO descriptionContent = new ContentDTO(null, userProfileDTO.getUsername(), null, ContentKindType.user_description, null, "", Constants.DEFAULT_LOCALE,
					new Date(), ContentStatusType.active);
			userDTO.setDescriptionContent(descriptionContent);
		}
		userDTO.getDescriptionContent().setText(view.getDescription());
		userProfileDTO.setDescriptionContent(userDTO.getDescriptionContent());
		if (Utils.hasText(view.getNewEmail())) {
			userProfileDTO.setNewEmail(view.getNewEmail());
		}
		if (Utils.hasText(view.getNewPassword())) {
			userProfileDTO.setPassword(view.getNewPassword());
			userProfileDTO.setEmail(view.getCurrentPassword()); //we use email field to pass password, just to verify the user
		}
		
		userProfileService.updateUserProfile(userProfileDTO, new AsyncCallback<UserDTO>() {
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException) {
	                ValidationException ex = (ValidationException) caught;
	                logger.log(Level.SEVERE, "Server side validation failed: ", ex);
	                validator.processServerErrors(ex);
	            } else {
	            	logger.log(Level.SEVERE, "Error updating UserProfile: ", caught);
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.account_update_failed)));
	            }
			}
			@Override
			public void onSuccess(UserDTO result) {
				logger.log(Level.FINE, "doSave onSuccess");
				LoginPresenter loginPresenter = (LoginPresenter)LocalStorage.getInstance().getPresenter(PresenterName.login.name());
				if (loginPresenter != null) {
					loginPresenter.setUserDTO(result);
				}
				eventBus.fireEvent(new UserProfileUpdatedEvent(result));
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.account_updated)));
			}
		});
	}

	private void doEditUserProfileCancelled() {
		History.back();
	}
	
	private void bind() {
	}

	@Override
	public UserDTO getUserDTO() {
		return userDTO;
	}

	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}