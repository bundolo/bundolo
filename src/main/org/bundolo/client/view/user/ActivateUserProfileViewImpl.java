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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.strings.EmailValidator;

public class ActivateUserProfileViewImpl extends Composite implements ActivateUserProfileView {
	
	private static final Logger logger = Logger.getLogger(ActivateUserProfileViewImpl.class.getName());

	@UiTemplate("ActivateUserProfileView.ui.xml")
	interface ActivateContentViewUiBinder extends UiBinder<Widget, ActivateUserProfileViewImpl> {}
	private static ActivateContentViewUiBinder uiBinder = 
			GWT.create(ActivateContentViewUiBinder.class);

	@UiField TextBox activationEmail;
	@UiField TextBox activationCode;
	@UiField RaphaelButtonWidget activateUserProfileButton;
	@UiField RaphaelButtonWidget cancelActivateUserProfileButton;
	@UiField Label emailAddressLabel;
	@UiField Label activationCodeLabel;
	
	private Presenter presenter;

	public ActivateUserProfileViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		emailAddressLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_address));
		activationCodeLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.activation_code));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.email_address.name(), new EmailValidator(activationEmail).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(emailAddressLabel, LabelType.email_address)));
		presenter.getValidator().addValidators(LabelType.activation_code.name(), new NotEmptyValidatorExtended(activationCode).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(activationCodeLabel, LabelType.activation_code)));
		//TODO email validation, code length validation probably (consider security, helping to guess code format if validation gives too much info)
	}

	@UiHandler("activateUserProfileButton")
	void onActivateUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onActivateUserProfileButtonClicked();
		}
	}

	@UiHandler("cancelActivateUserProfileButton")
	void onCancelActivateUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelActivateUserProfileButtonClicked();
		}
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public String getActivationEmail() {
		return activationEmail.getValue();
	}

	@Override
	public String getActivationCode() {
		return activationCode.getValue();
	}
} 