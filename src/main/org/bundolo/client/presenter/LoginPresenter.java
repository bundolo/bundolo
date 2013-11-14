package org.bundolo.client.presenter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.view.user.LoginView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class LoginPresenter implements Presenter, LoginView.Presenter {

    private static final Logger logger = Logger.getLogger(LoginPresenter.class.getName());

    private UserDTO userDTO;
    private final UserProfileServiceAsync userProfileService;
    private final HandlerManager eventBus;
    private final LoginView view;
    private final ValidationProcessor validator;

    public LoginPresenter(UserProfileServiceAsync userProfileService, HandlerManager eventBus, LoginView view) {
	this.userProfileService = userProfileService;
	this.eventBus = eventBus;
	this.view = view;
	validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
	this.view.setPresenter(this);
	bind();
    }

    @Override
    public void go(final HasWidgets container) {
	container.add(view.asWidget());
	if (userDTO != null) {
	    view.displayLogoutPanel(userDTO.getUsername());
	} else {
	    view.displayLoginPanel();
	}
    }

    private void bind() {

    }

    @Override
    public UserDTO getUserDTO() {
	return userDTO;
    }

    @Override
    public void onRegisterButtonClicked() {
	Utils.setHistoryTokenIfNeeded(PresenterName.addUser);
    }

    @Override
    public void onLoginButtonClicked() {
	if (validator.validate()) {
	    doLogin();
	}
    }

    @Override
    public void onGeneratePasswordButtonClicked() {
	view.displayGeneratePasswordPanel();
    }

    @Override
    public void onUserProfileButtonClicked() {
	UserProfilePresenter userPresenter = (UserProfilePresenter) LocalStorage.getInstance().getPresenter(
		PresenterName.user.name());
	userPresenter.setUserDTO(userDTO);
	Utils.setHistoryTokenIfNeeded(PresenterName.user);
    }

    @Override
    public void onLogoutButtonClicked() {
	doLogout();
    }

    private void doLogin() {
	logger.log(Level.FINE, "doLogin start");
	userProfileService.login(view.getUsername(), view.getPassword(), view.getRememberMe(),
		new AsyncCallback<UserDTO>() {
		    @Override
		    public void onFailure(Throwable caught) {
			logger.log(Level.SEVERE, "Error authenticating user: ", caught);
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
				.getLabel(LabelType.login_failed)));
		    }

		    @Override
		    public void onSuccess(UserDTO result) {
			logger.log(Level.FINE, "login user onSuccess: " + result);
			if (result != null) {
			    logger.log(Level.FINE, "sessionId: " + result.getSessionId());
			    Cookies.setCookie(Constants.SESSION_ID_COOKIE_NAME, result.getSessionId(),
				    Utils.addMinutes(new Date(), 15));
			    if (view.getRememberMe()) {
				Cookies.setCookie(Constants.REMEMBER_ME_COOKIE_NAME, result.getRememberMeCookie(),
					Utils.addDays(new Date(), 14));
			    }
			    userDTO = result;
			    view.displayLogoutPanel(result.getUsername());
			    eventBus.fireEvent(new UserAccessRightsUpdatedEvent());
			    // TODO start session timeout timer here
			} else {
			    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance()
				    .getMessageResource().getLabel(LabelType.login_failed)));
			}
			// TODO set cookie, use permanent
			// this is plain cookie: String msg =
			// Cookies.getCookie("MyCookie");
			// Cookies.setCookie("MyCookie",msg);
		    }
		});
    }

    private void doLogout() {
	userProfileService.logout(new AsyncCallback<Void>() {
	    @Override
	    public void onFailure(Throwable caught) {
		logger.log(Level.SEVERE, "Error logging out user: ", caught);
		eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			.getLabel(LabelType.logout_failed)));
	    }

	    @Override
	    public void onSuccess(Void result) {
		logger.log(Level.FINE, "logout user onSuccess: " + result);
		Cookies.removeCookie(Constants.SESSION_ID_COOKIE_NAME);
		Cookies.removeCookie(Constants.REMEMBER_ME_COOKIE_NAME);
		userDTO = null;
		view.displayLoginPanel();
		eventBus.fireEvent(new UserAccessRightsUpdatedEvent());
	    }
	});
    }

    @Override
    public void onLoginWithFacebookButtonClicked() {
	// TODO
	Window.alert("onLoginWithFacebookButtonClicked");
    }

    @Override
    public void onSendPasswordButtonClicked() {
	// TODO
	userProfileService.sendNewPassword(view.getEmail(), new AsyncCallback<Boolean>() {
	    @Override
	    public void onFailure(Throwable caught) {
		if (caught instanceof ValidationException) {
		    ValidationException ex = (ValidationException) caught;
		    logger.log(Level.FINE, "Server side validation failed: ", ex);
		    validator.processServerErrors(ex);
		} else {
		    logger.log(Level.SEVERE, "Error sending new password: ", caught);
		    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			    .getLabel(LabelType.password_sending_failed)));
		}
	    }

	    @Override
	    public void onSuccess(Boolean result) {
		logger.log(Level.FINE, "sendNewPassword onSuccess");
		if (result) {
		    // TODO define what should happen after password
		    // sending
		    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			    .getLabel(LabelType.password_sent)));
		} else {
		    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			    .getLabel(LabelType.user_not_found)));
		}
	    }
	});
    }

    @Override
    public void onSendPasswordCancelButtonClicked() {
	view.displayLoginPanel();
    }

    @Override
    public void setUserDTO(UserDTO userDTO) {
	this.userDTO = userDTO;
	view.displayLogoutPanel(userDTO.getUsername());
    }

    @Override
    public ValidationProcessor getValidator() {
	return validator;
    }
}