package org.bundolo.client.validation;

import com.axeiya.gwtckeditor.client.CKEditor;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.Validator;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

public class NotEmptyValidatorExtended extends Validator<NotEmptyValidatorExtended> {

	private TextBoxBase textBox = null;
	private SuggestBox suggestBox = null;
	private CKEditor cKEditor = null;
	
	/**
	 * Default mode uses trimming
	 */
	private boolean trim = true;
	
	public NotEmptyValidatorExtended(TextBoxBase text ) {
		this(text, null);
	}
	
	public NotEmptyValidatorExtended(TextBoxBase text, String customMsgKey ) {
		this.textBox = text;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public NotEmptyValidatorExtended(SuggestBox suggest) {
		this(suggest, null);
	}
	
	public NotEmptyValidatorExtended(SuggestBox suggest, String customMsgKey) {
		this.suggestBox = suggest;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public NotEmptyValidatorExtended(TextBoxBase text, boolean trimBeforeValidation ) {
		this(text, trimBeforeValidation, null);
	}
	
	public NotEmptyValidatorExtended(TextBoxBase text, boolean trimBeforeValidation, String customMsgKey) {
		this.textBox = text;
		this.trim = trimBeforeValidation;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public NotEmptyValidatorExtended(SuggestBox suggest, boolean trimBeforeValidation) {
		this(suggest, trimBeforeValidation, null);
	}
	
	public NotEmptyValidatorExtended(SuggestBox suggest, boolean trimBeforeValidation, String customMsgKey) {
		this.suggestBox = suggest;
		this.trim = trimBeforeValidation;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public NotEmptyValidatorExtended(CKEditor cKEditor ) {
		this(cKEditor, null);
	}
	
	public NotEmptyValidatorExtended(CKEditor cKEditor, String customMsgKey ) {
		this.cKEditor = cKEditor;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public NotEmptyValidatorExtended(CKEditor cKEditor, boolean trimBeforeValidation ) {
		this(cKEditor, trimBeforeValidation, null);
	}
	
	public NotEmptyValidatorExtended(CKEditor cKEditor, boolean trimBeforeValidation, String customMsgKey) {
		this.cKEditor = cKEditor;
		this.trim = trimBeforeValidation;
		this.setCustomMsgKey(customMsgKey);
	}
	
	
	@Override
	public void invokeActions(ValidationResult result) {
		if(textBox != null) {
			for(ValidationAction<TextBoxBase> va : this.getFailureActions())
				va.invoke(result, textBox);
		} else if(suggestBox != null) {
			for(ValidationAction<SuggestBox> va : this.getFailureActions())
				va.invoke(result, suggestBox);
		} else if(cKEditor != null) {
			for(ValidationAction<CKEditor> va : this.getFailureActions())
				va.invoke(result, cKEditor);
		}
	}

	@Override
	public <V extends ValidationMessages> ValidationResult validate(V messages) {
		
		String text = "";
		if(suggestBox != null) {
			text = suggestBox.getText();
		} else if(textBox != null) {
			text = textBox.getText();
		} else if(cKEditor != null) {
			text = cKEditor.getHTML();
		}
		
		if(trim)
			text = text.trim();
		
		if(text.length() == 0) {
			return new ValidationResult(getErrorMessage(messages, messages.getStandardMessages().notEmpty()));
		}
		return null;
	}

}
