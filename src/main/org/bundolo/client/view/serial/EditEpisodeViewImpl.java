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

public class EditEpisodeViewImpl extends Composite implements EditEpisodeView {
	
	private static final Logger logger = Logger.getLogger(EditEpisodeViewImpl.class.getName());

	@UiTemplate("EditEpisodeView.ui.xml")
	interface EditEpisodeViewUiBinder extends UiBinder<Widget, EditEpisodeViewImpl> {}
	private static EditEpisodeViewUiBinder uiBinder = GWT.create(EditEpisodeViewUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField(provided = true)
	CKEditor text = new CKEditor();

	@UiField
	RaphaelButtonWidget saveEpisodeButton;

	@UiField
	RaphaelButtonWidget cancelEpisodeButton;
	
	@UiField Label nameLabel;
	@UiField Label textLabel;

	private Presenter presenter;

	public EditEpisodeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.episode_name));
		textLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.episode_text));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.episode_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.episode_name)));
		presenter.getValidator().addValidators(LabelType.episode_text.name(), new NotEmptyValidatorExtended(text).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(textLabel, LabelType.episode_text)));
	}

	@UiHandler("saveEpisodeButton")
	void onSaveEpisodeButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveEpisodeButtonClicked();
		}
	}

	@UiHandler("cancelEpisodeButton")
	void onCancelEpisodeButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelEpisodeButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			name.setValue(presenter.getContentDTO().getName());
			text.setHTML(presenter.getContentDTO().getText());
		}
		return this;
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String getText() {
		return text.getHTML();
	}

} 