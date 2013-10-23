package org.bundolo.client.view.user;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.validation.LabelTextActionExtended;
import org.bundolo.client.validation.NotEmptyValidatorExtended;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.standard.RegularExpressionValidator;
import eu.maydu.gwt.validation.client.validators.strings.EmailValidator;
import eu.maydu.gwt.validation.client.validators.strings.NameValidator;
import eu.maydu.gwt.validation.client.validators.strings.StringLengthValidator;

public class AddUserProfileViewImpl extends Composite implements AddUserProfileView {
	
	private static final Logger logger = Logger.getLogger(AddUserProfileViewImpl.class.getName());

	@UiTemplate("AddUserProfileView.ui.xml")
	interface AddContentViewUiBinder extends UiBinder<Widget, AddUserProfileViewImpl> {}
	private static AddContentViewUiBinder uiBinder = 
			GWT.create(AddContentViewUiBinder.class);

	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField TextBox firstEmail;
	@UiField RaphaelButtonWidget saveUserProfileButton;
	@UiField RaphaelButtonWidget cancelUserProfileButton;
	@UiField Label displayNameLabel;
	@UiField Label emailAddressLabel;
	@UiField Label passwordLabel;
	
	//TODO add captcha maybe

	private Presenter presenter;

	public AddUserProfileViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		displayNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.display_name));
		emailAddressLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_address));
		passwordLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.password));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.display_name.name(), new NotEmptyValidatorExtended(username).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)),
				new NameValidator(username).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)),
				new StringLengthValidator(username, 3, 30).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)),
				new RegularExpressionValidator(username, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.anonymous_user), LocalStorage.getInstance().getMessageResource().getLabel(LabelType.validator_username_forbidden)).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)));
		presenter.getValidator().addValidators(LabelType.email_address.name(), new EmailValidator(firstEmail).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(emailAddressLabel, LabelType.email_address)));
		presenter.getValidator().addValidators(LabelType.password.name(), new NotEmptyValidatorExtended(password).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(passwordLabel, LabelType.password)),
				new StringLengthValidator(password, 5, 30).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(passwordLabel, LabelType.password)));
	}

	@UiHandler("saveUserProfileButton")
	void onSaveUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveUserProfileButtonClicked();
		}
	}

	@UiHandler("cancelUserProfileButton")
	void onCancelUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelUserProfileButtonClicked();
		}
	}

	public Widget asWidget() {
		return this;
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
	public String getEmail() {
		return firstEmail.getValue();
	}

} 