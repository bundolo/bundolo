package org.bundolo.client.view.news;

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

public class EditNewsViewImpl extends Composite implements EditNewsView {
	
	private static final Logger logger = Logger.getLogger(EditNewsViewImpl.class.getName());

	@UiTemplate("EditNewsView.ui.xml")
	interface EditNewsViewUiBinder extends UiBinder<Widget, EditNewsViewImpl> {}
	private static EditNewsViewUiBinder uiBinder = GWT.create(EditNewsViewUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField(provided = true)
	CKEditor description = new CKEditor();

	@UiField
	RaphaelButtonWidget saveNewsButton;

	@UiField
	RaphaelButtonWidget cancelNewsButton;
	
	@UiField Label nameLabel;
	@UiField Label descriptionLabel;

	private Presenter presenter;

	public EditNewsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_name));
		descriptionLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_description));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.news_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.news_name)));
		presenter.getValidator().addValidators(LabelType.news_description.name(), new NotEmptyValidatorExtended(description).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(descriptionLabel, LabelType.news_description)));
	}

	@UiHandler("saveNewsButton")
	void onSaveNewsButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveNewsButtonClicked();
		}
	}

	@UiHandler("cancelNewsButton")
	void onCancelNewsButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelNewsButtonClicked();
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