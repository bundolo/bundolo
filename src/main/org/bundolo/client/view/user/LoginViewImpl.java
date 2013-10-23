package org.bundolo.client.view.user;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.validation.LabelTextActionExtended;
import org.bundolo.client.validation.NotEmptyValidatorExtended;
import org.bundolo.client.widget.ConditionalPanel;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.standard.RegularExpressionValidator;

public class LoginViewImpl extends Composite implements LoginView {

	private static final Logger logger = Logger.getLogger(LoginViewImpl.class.getName());

	@UiTemplate("LoginView.ui.xml")
	interface LoginViewUiBinder extends UiBinder<Widget, LoginViewImpl> {}
	private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

	@UiField
	ConditionalPanel loginPanel;
	@UiField
	ConditionalPanel logoutPanel;
	@UiField
	ConditionalPanel generatePasswordPanel;
	@UiField
	TextBox username;
	@UiField
	TextBox email;
	@UiField
	PasswordTextBox password;
	@UiField
	CheckBox rememberMe;
	@UiField
	RaphaelButtonWidget registerButton;
	@UiField
	RaphaelButtonWidget loginButton;
	@UiField
	RaphaelButtonWidget generatePasswordButton;
	/* @UiField */Button loginWithFacebookButton;
	@UiField
	RaphaelButtonWidget userProfileButton;
	@UiField
	RaphaelButtonWidget logoutButton;
	@UiField
	RaphaelButtonWidget sendPasswordButton;
	@UiField
	RaphaelButtonWidget sendPasswordCancelButton;
	@UiField
	Label currentUserMessage;
	@UiField
	Label usernameLabel;
	@UiField
	Label passwordLabel;
//	@UiField
//	Label rememberMeLabel;
	@UiField
	Label emailLabel;

	private Presenter presenter;

	public LoginViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		addLabelHandlers();
		usernameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.login_username_or_email));
		passwordLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.login_password));
		//rememberMeLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.remember_me));
		emailLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.login_email));
		rememberMe.setTitle(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.remember_me));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.login_username_or_email.name(), new NotEmptyValidatorExtended(username).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(usernameLabel, LabelType.login_username_or_email)),
				new RegularExpressionValidator(username, "^(?!" + LocalStorage.getInstance().getMessageResource().getLabel(LabelType.anonymous_user) + "$).*", LocalStorage.getInstance().getMessageResource().getLabel(LabelType.validator_username_forbidden)).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(usernameLabel, LabelType.login_username_or_email)));
		presenter.getValidator().addValidators(LabelType.login_password.name(), new NotEmptyValidatorExtended(password).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(passwordLabel, LabelType.login_password)));
	}

	public Widget asWidget() {
		return this;
	}

	@UiHandler("registerButton")
	void onRegisterButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onRegisterButtonClicked();
		}
	}

	@UiHandler("loginButton")
	void onLoginButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onLoginButtonClicked();
		}
	}

	@UiHandler("generatePasswordButton")
	void onGeneratePasswordButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onGeneratePasswordButtonClicked();
		}
	}

	// @UiHandler("loginWithFacebookButton")
	void onLoginWithFacebookButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onLoginWithFacebookButtonClicked();
		}
	}

	@UiHandler("userProfileButton")
	void onUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onUserProfileButtonClicked();
		}
	}

	@UiHandler("logoutButton")
	void onLogoutButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onLogoutButtonClicked();
		}
	}

	@UiHandler("sendPasswordButton")
	void onSendPasswordButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSendPasswordButtonClicked();
		}
	}

	@UiHandler("sendPasswordCancelButton")
	void onSendPasswordCancelButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSendPasswordCancelButtonClicked();
		}
	}

	@Override
	public String getUsername() {
		return username.getValue();
	}

	@Override
	public String getPassword() {
		return password.getValue();
	}

	@Override
	public Boolean getRememberMe() {
		return rememberMe.getValue();
	}

	@Override
	public String getEmail() {
		return email.getValue();
	}

	public void displayLoginPanel() {
		currentUserMessage.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.logged_out));
		loginPanel.setVisible(true);
		logoutPanel.setVisible(false);
		generatePasswordPanel.setVisible(false);
	}

	public void displayLogoutPanel(String username) {
		currentUserMessage.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.logged_in) + " " + username);
		loginPanel.setVisible(false);
		logoutPanel.setVisible(true);
		generatePasswordPanel.setVisible(false);
	}

	public void displayGeneratePasswordPanel() {
		loginPanel.setVisible(false);
		logoutPanel.setVisible(false);
		generatePasswordPanel.setVisible(true);
	}
	
	private void addLabelHandlers() {
		username.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				usernameLabel.setVisible(false);
			}
			
		});
		password.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				passwordLabel.setVisible(false);
			}
			
		});
		email.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				emailLabel.setVisible(false);
			}
			
		});
		usernameLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				usernameLabel.setVisible(false);
				username.setFocus(true);
			}
			
		});
		passwordLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				passwordLabel.setVisible(false);
				password.setFocus(true);
			}
			
		});
		emailLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				emailLabel.setVisible(false);
				email.setFocus(true);
			}
			
		});
	}

}