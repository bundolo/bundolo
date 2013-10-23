package org.bundolo.client.view.user;

import java.util.Date;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.validation.LabelTextActionExtended;
import org.bundolo.client.validation.NotEmptyValidatorExtended;
import org.bundolo.client.widget.EnumListBox;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.UserProfileGenderType;

import com.axeiya.gwtckeditor.client.CKEditor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.datetime.LocalizedDateValidator;
import eu.maydu.gwt.validation.client.validators.standard.DummyValidator;
import eu.maydu.gwt.validation.client.validators.standard.NotEmptyValidator;
import eu.maydu.gwt.validation.client.validators.strings.EmailValidator;
import eu.maydu.gwt.validation.client.validators.strings.NameValidator;
import eu.maydu.gwt.validation.client.validators.strings.StringEqualsValidator;
import eu.maydu.gwt.validation.client.validators.strings.StringLengthValidator;

public class EditUserProfileViewImpl extends Composite implements EditUserProfileView {
	
	private static final Logger logger = Logger.getLogger(EditUserProfileViewImpl.class.getName());

	@UiTemplate("EditUserProfileView.ui.xml")
	interface EditContentViewUiBinder extends UiBinder<Widget, EditUserProfileViewImpl> {}
	private static EditContentViewUiBinder uiBinder = 
			GWT.create(EditContentViewUiBinder.class);

	@UiField TextBox username;
	@UiField PasswordTextBox currentPassword;
	@UiField PasswordTextBox newPassword;
	@UiField PasswordTextBox retypePassword;
	@UiField TextBox newEmail;
	@UiField TextBox retypeEmail;
	@UiField TextBox firstName;
	@UiField TextBox lastName;
	@UiField Label publicDataLabel;
	@UiField Label displayNameLabel;
	@UiField Label descriptionLabel;
	@UiField Label avatarLabel;
	@UiField Label personalDataLabel;
	@UiField Label firstNameLabel;
	@UiField Label lastNameLabel;
	@UiField Label genderLabel;
	@UiField Label birthDateLabel;
	@UiField Label showPersonalLabel;
	@UiField Label passwordInstructionsLabel;
	@UiField Label currentPasswordLabel;
	@UiField Label newPasswordLabel;
	@UiField Label retypePasswordLabel;
	@UiField Label emailInstructionsLabel;
	@UiField Label newEmailLabel;
	@UiField Label retypeEmailLabel;
	
	@UiField(provided = true)
    CKEditor description = new CKEditor();
	
	@UiField(provided = true)
	EnumListBox<UserProfileGenderType> gender = new EnumListBox<UserProfileGenderType>(UserProfileGenderType.class);

	@UiField(provided = true)
	DateBox birthDate = new DateBox();
	@UiField CheckBox showPersonal;
	@UiField TextBox avatarUrl;
	@UiField RaphaelButtonWidget saveUserProfileButton;
	@UiField RaphaelButtonWidget cancelUserProfileButton;

	private Presenter presenter;

	public EditUserProfileViewImpl() {
		birthDate.setFormat(new DateBox.DefaultFormat(LocalStorage.getInstance().getDateTimeformat()));
		initWidget(uiBinder.createAndBindUi(this));
		publicDataLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.public_data));
		displayNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.display_name));
		descriptionLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.user_description));
		avatarLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.avatar));
		personalDataLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.personal_data));
		firstNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.first_name));
		lastNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.last_name));
		genderLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.gender));
		birthDateLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.birth_date));
		showPersonalLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.show_personal));
		passwordInstructionsLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.password_instructions));
		currentPasswordLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.current_password));
		newPasswordLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.new_password));
		retypePasswordLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.retype_password));
		emailInstructionsLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_instructions));
		newEmailLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.new_email));
		retypeEmailLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.retype_email));
	}

	public void setPresenter(Presenter presenter) {
		
		//validator disabled for now
//		presenter.getValidator().addValidators(LabelType.display_name.name(), new NotEmptyValidatorExtended(username).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)),
//				new NameValidator(username).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)),
//				new StringLengthValidator(username, 3, 30).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(displayNameLabel, LabelType.display_name)));
		//fields hidden for now
		username.setVisible(false);
		displayNameLabel.setVisible(false);
		avatarUrl.setVisible(false);
		avatarLabel.setVisible(false);
		
		
		LocalizedDateValidator dateValidator = new LocalizedDateValidator(birthDate.getTextBox(), LocalStorage.getInstance().getDateTimeformat()).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(birthDateLabel, LabelType.birth_date));
		dateValidator.setRequired(false);
    	presenter.getValidator().addValidators(LabelType.birth_date.name(), dateValidator);
    	EmailValidator emailValidator = new EmailValidator(newEmail).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(newEmailLabel, LabelType.new_email));
    	emailValidator.setRequired(false);
    	presenter.getValidator().addValidators(LabelType.new_email.name(), emailValidator);
    	StringLengthValidator stringLengthValidator = new StringLengthValidator(newPassword, 5, 30).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(newPasswordLabel, LabelType.new_password));
    	stringLengthValidator.setRequired(false);
    	presenter.getValidator().addValidators(LabelType.new_password.name(), stringLengthValidator);
    	StringEqualsValidator passwordStringEqualsValidator = new StringEqualsValidator(newPassword, retypePassword).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(newPasswordLabel, LabelType.new_password)).
				addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(retypePasswordLabel, LabelType.retype_password));
    	passwordStringEqualsValidator.setRequired(false);
		presenter.getValidator().addValidators(LabelType.validator_passwordsMustMatch.name(), passwordStringEqualsValidator);
		StringEqualsValidator emailStringEqualsValidator = new StringEqualsValidator(newEmail, retypeEmail).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(newEmailLabel, LabelType.new_email)).
				addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(retypeEmailLabel, LabelType.retype_email));
		emailStringEqualsValidator.setRequired(false);
		presenter.getValidator().addValidators(LabelType.validator_emailsMustMatch.name(), emailStringEqualsValidator);
		presenter.getValidator().addValidators(LabelType.current_password.name(), new DummyValidator(currentPassword).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(currentPasswordLabel, LabelType.current_password)));
		this.presenter = presenter;
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
		if ((presenter != null) && (presenter.getUserDTO() != null)) {
			username.setValue(presenter.getUserDTO().getUsername());
			//email.setValue(presenter.getUserDTO().getEmail());
			firstName.setValue(presenter.getUserDTO().getFirstName());
			lastName.setValue(presenter.getUserDTO().getLastName());
			gender.setValue(presenter.getUserDTO().getGender());
			birthDate.setValue(presenter.getUserDTO().getBirthDate());
			showPersonal.setValue(presenter.getUserDTO().getShowPersonal());
			avatarUrl.setValue(presenter.getUserDTO().getAvatarUrl());
			if (presenter.getUserDTO().getDescriptionContent() != null) {
				description.setHTML(presenter.getUserDTO().getDescriptionContent().getText());
			}
		}
		//TODO port this from gxt to gwt
		//email.addValidator(new RegExValidator("^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$", "Only valid email addresses allowed")); 
		return this;
	}

	@Override
	public String getUsername() {
		return username.getValue();
	}

	@Override
	public String getNewPassword() {
		return newPassword.getValue();
	}

	@Override
	public String getNewEmail() {
		return newEmail.getValue();
	}

	@Override
	public String getFirstName() {
		return firstName.getValue();
	}

	@Override
	public String getLastName() {
		return lastName.getValue();
	}

	@Override
	public UserProfileGenderType getGender() {
		return gender.getValue();
	}

	@Override
	public Date getBirthDate() {
		return birthDate.getValue();
	}

	@Override
	public Boolean getShowPersonal() {
		return showPersonal.getValue();
	}

	@Override
	public String getAvatarUrl() {
		return avatarUrl.getValue();
	}

	@Override
	public String getDescription() {
		return description.getHTML();
	}

	@Override
	public String getCurrentPassword() {
		return currentPassword.getValue();
	}

} 