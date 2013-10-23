package org.bundolo.client.view.user;

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

public class EditMessageViewImpl extends Composite implements EditMessageView {
	
	private static final Logger logger = Logger.getLogger(EditMessageViewImpl.class.getName());

	@UiTemplate("EditMessageView.ui.xml")
	interface EditMessageViewUiBinder extends UiBinder<Widget, EditMessageViewImpl> {}
	private static EditMessageViewUiBinder uiBinder = GWT.create(EditMessageViewUiBinder.class);
	
	@UiField
	TextBox title;
	
	@UiField(provided = true)
	CKEditor text = new CKEditor();

	@UiField
	RaphaelButtonWidget sendMessageButton;

	@UiField
	RaphaelButtonWidget cancelMessageButton;
	
	@UiField Label titleLabel;
	@UiField Label textLabel;

	private Presenter presenter;

	public EditMessageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		titleLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.message_title));
		textLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.message_text));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.message_title.name(), new NotEmptyValidatorExtended(title).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(titleLabel, LabelType.message_title)));
		presenter.getValidator().addValidators(LabelType.message_text.name(), new NotEmptyValidatorExtended(text).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(textLabel, LabelType.message_text)));
	}

	@UiHandler("sendMessageButton")
	void onSendMessageButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSendMessageButtonClicked();
		}
	}

	@UiHandler("cancelMessageButton")
	void onCancelMessageButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelMessageButtonClicked();
		}
	}

	public Widget asWidget() {
		title.setValue("");
		text.setHTML("");
		return this;
	}

	@Override
	public String getTitle() {
		return title.getValue();
	}

	@Override
	public String getText() {
		return text.getHTML();
	}

} 