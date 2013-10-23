package org.bundolo.client.view.connection;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.validation.EitherNotEmptyValidator;
import org.bundolo.client.validation.LabelTextActionExtended;
import org.bundolo.client.validation.NotEmptyValidatorExtended;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.LabelType;

import com.axeiya.gwtckeditor.client.CKEditor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.strings.EmailValidator;

public class EditConnectionViewImpl extends Composite implements EditConnectionView {
	
	private static final Logger logger = Logger.getLogger(EditConnectionViewImpl.class.getName());

	@UiTemplate("EditConnectionView.ui.xml")
	interface EditConnectionViewUiBinder extends UiBinder<Widget, EditConnectionViewImpl> {}
	private static EditConnectionViewUiBinder uiBinder = GWT.create(EditConnectionViewUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField(provided = true)
	CKEditor description = new CKEditor();
	
	@UiField
	TextBox email;
	
	@UiField
	TextBox url;

	@UiField
	RaphaelButtonWidget saveConnectionButton;

	@UiField
	RaphaelButtonWidget cancelConnectionButton;
	
	@UiField Label nameLabel;
	@UiField Label descriptionLabel;
	@UiField Label emailLabel;
	@UiField Label urlLabel;

	private Presenter presenter;

	public EditConnectionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_name));
		descriptionLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_description));
		emailLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_email));
		urlLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.connection_url));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.connection_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.connection_name)));
    	presenter.getValidator().addValidators(LabelType.connection_description.name(), new NotEmptyValidatorExtended(description).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(descriptionLabel, LabelType.connection_description)));
    	EmailValidator emailValidator = new EmailValidator(email).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(emailLabel, LabelType.connection_email));
    	emailValidator.setRequired(false);
    	presenter.getValidator().addValidators(LabelType.connection_email.name(), emailValidator);
    	presenter.getValidator().addValidators(LabelType.validator_eitherNotEmpty.name(), new EitherNotEmptyValidator(email, url).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(emailLabel, LabelType.connection_email)).addActionForFailure(new LabelTextActionExtended(urlLabel, LabelType.connection_url)));
	}

	@UiHandler("saveConnectionButton")
	void onSaveConnectionButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveConnectionButtonClicked();
		}
	}

	@UiHandler("cancelConnectionButton")
	void onCancelConnectionButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelConnectionButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			name.setValue(presenter.getConnectionDTO().getDescriptionContent().getName());
			description.setHTML(presenter.getConnectionDTO().getDescriptionContent().getText());
			email.setValue(presenter.getConnectionDTO().getEmail());
			url.setValue(presenter.getConnectionDTO().getUrl());
		}
		return this;
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String getDescription() {
		return description.getHTML();
	}

	@Override
	public String getEmail() {
		return email.getValue();
	}

	@Override
	public String getUrl() {
		return url.getValue();
	}

} 