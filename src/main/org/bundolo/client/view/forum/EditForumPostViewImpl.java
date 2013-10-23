package org.bundolo.client.view.forum;

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
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;

public class EditForumPostViewImpl extends Composite implements EditForumPostView {
	
	private static final Logger logger = Logger.getLogger(EditForumPostViewImpl.class.getName());

	@UiTemplate("EditForumPostView.ui.xml")
	interface EditForumPostViewUiBinder extends UiBinder<Widget, EditForumPostViewImpl> {}
	private static EditForumPostViewUiBinder uiBinder = GWT.create(EditForumPostViewUiBinder.class);

	@UiField(provided = true)
	CKEditor text = new CKEditor();

	@UiField
	RaphaelButtonWidget saveForumButton;

	@UiField
	RaphaelButtonWidget cancelForumButton;
	
	@UiField Label textLabel;

	private Presenter presenter;

	public EditForumPostViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		textLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_text));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.forum_post_text.name(), new NotEmptyValidatorExtended(text).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(textLabel, LabelType.forum_post_text)));
	}

	@UiHandler("saveForumButton")
	void onSaveForumButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveForumPostButtonClicked();
		}
	}

	@UiHandler("cancelForumButton")
	void onCancelForumButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelForumPostButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			text.setHTML(presenter.getContentDTO().getText());
		}
		return this;
	}

	public String getText() {
		return text.getHTML();
	}

} 