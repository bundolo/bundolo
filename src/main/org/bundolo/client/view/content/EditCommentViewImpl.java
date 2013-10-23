package org.bundolo.client.view.content;

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
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;

public class EditCommentViewImpl extends Composite implements EditCommentView {
	
	private static final Logger logger = Logger.getLogger(EditCommentViewImpl.class.getName());

	@UiTemplate("EditCommentView.ui.xml")
	interface EditCommentViewUiBinder extends UiBinder<Widget, EditCommentViewImpl> {}
	private static EditCommentViewUiBinder uiBinder = 
			GWT.create(EditCommentViewUiBinder.class);

	@UiField(provided = true)
	CKEditor text = new CKEditor();
	
	@UiField Label textLabel;

	@UiField
	RaphaelButtonWidget saveCommentButton;

	@UiField
	RaphaelButtonWidget cancelCommentButton;

	private Presenter presenter;

	public EditCommentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		textLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_text));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.comment_text.name(), new NotEmptyValidatorExtended(text).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(textLabel, LabelType.comment_text)));
	}

	@UiHandler("saveCommentButton")
	void onSaveCommentButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveCommentButtonClicked();
		}
	}

	@UiHandler("cancelCommentButton")
	void onCancelCommentButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelCommentButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			//name.setText(presenter.getContentDTO().getName());
			text.setHTML(presenter.getContentDTO().getText());
			//kind.setValue(presenter.getContentDTO().getKind());
			//contentStatus.setValue(presenter.getContentDTO().getContentStatus());
		}
		return this;
	}

//	@Override
//	public ContentKindType getKind() {
//		return kind.getValue();
//	}

	@Override
	public String getText() {
		return text.getHTML();
	}

//	@Override
//	public ContentStatusType getContentStatus() {
//		return contentStatus.getValue();
//	}
//
//	@Override
//	public String getName() {
//		return name.getValue();
//	}

} 