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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;
import eu.maydu.gwt.validation.client.validators.standard.NotEmptyValidator;

public class EditTextViewImpl extends Composite implements EditTextView {
	
    private static final Logger logger = Logger.getLogger(EditTextViewImpl.class.getName());

    @UiTemplate("EditTextView.ui.xml")
    interface EditTextViewUiBinder extends UiBinder<Widget, EditTextViewImpl> {}
    private static EditTextViewUiBinder uiBinder = 
    		GWT.create(EditTextViewUiBinder.class);
    
    @UiField
    TextBox title;
    
    @UiField
    TextBox description;
    
    @UiField(provided = true)
    CKEditor text = new CKEditor();
    
    @UiField
    RaphaelButtonWidget saveTextButton;
    
    @UiField
    RaphaelButtonWidget cancelTextButton;
    
    //TODO add labels
    @UiField
    Label titleLabel;
    
    @UiField
    Label descriptionLabel;
    
    @UiField
    Label textLabel;
    
    //private ValidationProcessor validator;
    
    private Presenter presenter;
    
    public EditTextViewImpl() {
    	initWidget(uiBinder.createAndBindUi(this));
    	titleLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_name));
    	descriptionLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_description));
    	textLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_text));
    }
    
    public void setPresenter(Presenter presenter) {
    	this.presenter = presenter;
    	presenter.getValidator().addValidators(LabelType.text_name.name(), new NotEmptyValidatorExtended(title).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(titleLabel, LabelType.text_name)));
    	presenter.getValidator().addValidators(LabelType.text_text.name(), new NotEmptyValidatorExtended(text).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(textLabel, LabelType.text_text)));
    }
    
    @UiHandler("saveTextButton")
    void onSaveTextButtonClicked(ClickEvent event) {
    	if (presenter != null) {
    		presenter.onSaveTextButtonClicked();
    	}
    }
    
    @UiHandler("cancelTextButton")
    void onCancelTextButtonClicked(ClickEvent event) {
    	if (presenter != null) {
    		presenter.onCancelTextButtonClicked();
    	}
    }
    
    public Widget asWidget() {
    	if (presenter != null) {
        	title.setText(presenter.getContentDTO().getName());
        	text.setHTML(presenter.getContentDTO().getText());
        	description.setText(presenter.getContentDTO().getDescriptionContent().getText());
    	}
    	return this;
    }
    
    @Override
    public String getText() {
    	return text.getHTML();
    }
    
    @Override
    public String getTitle() {
    	return title.getValue();
    }
    
    @Override
    public String getDescription() {
    	return description.getValue();
    }

} 