package org.bundolo.client.view.serial;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;

public class EditSerialViewImpl extends Composite implements EditSerialView {
	
	private static final Logger logger = Logger.getLogger(EditSerialViewImpl.class.getName());

	@UiTemplate("EditSerialView.ui.xml")
	interface EditSerialViewUiBinder extends UiBinder<Widget, EditSerialViewImpl> {}
	private static EditSerialViewUiBinder uiBinder = GWT.create(EditSerialViewUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField(provided = true)
	CKEditor description = new CKEditor();

	@UiField
	RaphaelButtonWidget saveSerialButton;

	@UiField
	RaphaelButtonWidget cancelSerialButton;
	
	@UiField
	Label nameLabel;
	@UiField
	Label descriptionLabel;

	private Presenter presenter;

	public EditSerialViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_name));
		descriptionLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.serial_description));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.serial_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.serial_name)));
	}

	@UiHandler("saveSerialButton")
	void onSaveSerialButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveSerialButtonClicked();
		}
	}

	@UiHandler("cancelSerialButton")
	void onCancelSerialButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelSerialButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			name.setValue(presenter.getContentDTO().getName());
			description.setHTML(presenter.getContentDTO().getText());
		}
		return this;
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String getText() {
		return description.getHTML();
	}

} 