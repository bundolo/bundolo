package org.bundolo.client.view.contest;

import java.util.Date;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
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
import com.google.gwt.user.datepicker.client.DateBox;

import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.datetime.LocalizedDateValidator;

public class EditContestViewImpl extends Composite implements EditContestView {
	
	private static final Logger logger = Logger.getLogger(EditContestViewImpl.class.getName());

	@UiTemplate("EditContestView.ui.xml")
	interface EditContestViewUiBinder extends UiBinder<Widget, EditContestViewImpl> {}
	private static EditContestViewUiBinder uiBinder = GWT.create(EditContestViewUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField(provided = true)
	CKEditor description = new CKEditor();
	
	@UiField(provided = true)
	DateBox expirationDate = new DateBox();

	@UiField
	RaphaelButtonWidget saveContestButton;

	@UiField
	RaphaelButtonWidget cancelContestButton;
	
	@UiField Label nameLabel;
	@UiField Label descriptionLabel;
	@UiField Label expirationDateLabel;

	private Presenter presenter;

	public EditContestViewImpl() {
		expirationDate.setFormat(new DateBox.DefaultFormat(LocalStorage.getInstance().getDateTimeformat()));
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_name));
		descriptionLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_description));
		expirationDateLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_expiration_date));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.contest_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.contest_name)));
		presenter.getValidator().addValidators(LabelType.contest_description.name(), new NotEmptyValidatorExtended(description).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(descriptionLabel, LabelType.contest_description)));
		LocalizedDateValidator dateValidator = new LocalizedDateValidator(expirationDate.getTextBox(), LocalStorage.getInstance().getDateTimeformat()).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(expirationDateLabel, LabelType.contest_expiration_date));
		dateValidator.setRequired(false);
    	presenter.getValidator().addValidators(LabelType.contest_expiration_date.name(), dateValidator);
	}

	@UiHandler("saveContestButton")
	void onSaveContestButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveContestButtonClicked();
		}
	}

	@UiHandler("cancelContestButton")
	void onCancelContestButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelContestButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			name.setValue(presenter.getContestDTO().getDescriptionContent().getName());
			description.setHTML(presenter.getContestDTO().getDescriptionContent().getText());
			expirationDate.setValue(presenter.getContestDTO().getExpirationDate());
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
	public Date getExpirationDate() {
		return expirationDate.getValue();
	}

} 