package org.bundolo.client.validation;

import com.axeiya.gwtckeditor.client.CKEditor;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.Validator;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

public class EitherNotEmptyValidator extends Validator<EitherNotEmptyValidator> {

	private TextBoxBase textBox1 = null;
	private TextBoxBase textBox2 = null;
	private SuggestBox suggestBox1 = null;
	private SuggestBox suggestBox2 = null;
	private CKEditor cKEditor1 = null;
	private CKEditor cKEditor2 = null;
	
	/**
	 * Default mode uses trimming
	 */
	private boolean trim = true;
	
	public EitherNotEmptyValidator(TextBoxBase text1, TextBoxBase text2) {
		this(text1, text2, null);
	}
	
	public EitherNotEmptyValidator(TextBoxBase text1, TextBoxBase text2, String customMsgKey ) {
		this.textBox1 = text1;
		this.textBox2 = text2;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public EitherNotEmptyValidator(SuggestBox suggest1, SuggestBox suggest2) {
		this(suggest1, suggest2, null);
	}
	
	public EitherNotEmptyValidator(SuggestBox suggest1, SuggestBox suggest2, String customMsgKey) {
		this.suggestBox1 = suggest1;
		this.suggestBox2 = suggest2;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public EitherNotEmptyValidator(TextBoxBase text1, TextBoxBase text2, boolean trimBeforeValidation ) {
		this(text1, text2, trimBeforeValidation, null);
	}
	
	public EitherNotEmptyValidator(TextBoxBase text1, TextBoxBase text2, boolean trimBeforeValidation, String customMsgKey) {
		this.textBox1 = text1;
		this.textBox2 = text2;
		this.trim = trimBeforeValidation;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public EitherNotEmptyValidator(SuggestBox suggest1, SuggestBox suggest2, boolean trimBeforeValidation) {
		this(suggest1, suggest2, trimBeforeValidation, null);
	}
	
	public EitherNotEmptyValidator(SuggestBox suggest1, SuggestBox suggest2, boolean trimBeforeValidation, String customMsgKey) {
		this.suggestBox1 = suggest1;
		this.suggestBox2 = suggest2;
		this.trim = trimBeforeValidation;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public EitherNotEmptyValidator(CKEditor cKEditor1, CKEditor cKEditor2) {
		this(cKEditor1, cKEditor2, null);
	}
	
	public EitherNotEmptyValidator(CKEditor cKEditor1, CKEditor cKEditor2, String customMsgKey ) {
		this.cKEditor1 = cKEditor1;
		this.cKEditor2 = cKEditor2;
		this.setCustomMsgKey(customMsgKey);
	}
	
	public EitherNotEmptyValidator(CKEditor cKEditor1, CKEditor cKEditor2, boolean trimBeforeValidation ) {
		this(cKEditor1, cKEditor2, trimBeforeValidation, null);
	}
	
	public EitherNotEmptyValidator(CKEditor cKEditor1, CKEditor cKEditor2, boolean trimBeforeValidation, String customMsgKey) {
		this.cKEditor1 = cKEditor1;
		this.cKEditor2 = cKEditor2;
		this.trim = trimBeforeValidation;
		this.setCustomMsgKey(customMsgKey);
	}
	
	
	@Override
	public void invokeActions(ValidationResult result) {
		if(textBox1 != null) {
			for(ValidationAction<TextBoxBase> va : this.getFailureActions()) {
				va.invoke(result, textBox1);
				va.invoke(result, textBox2);
			}
		} else if(suggestBox1 != null) {
			for(ValidationAction<SuggestBox> va : this.getFailureActions()) {
				va.invoke(result, suggestBox1);
				va.invoke(result, suggestBox2);
			}
		} else if(cKEditor1 != null) {
			for(ValidationAction<CKEditor> va : this.getFailureActions()) {
				va.invoke(result, cKEditor1);
				va.invoke(result, cKEditor2);
			}
		}
	}

	@Override
	public <V extends ValidationMessages> ValidationResult validate(V messages) {
		
		String text = "";
		if(suggestBox1 != null) {
			text = suggestBox1.getText() + suggestBox2.getText();
		} else if(textBox1 != null) {
			text = textBox1.getText() + textBox2.getText();
		} else if(cKEditor1 != null) {
			text = cKEditor1.getHTML() + cKEditor2.getHTML();
		}
		
		if(trim)
			text = text.trim();
		
		if(text.length() == 0) {
			return new ValidationResult(getErrorMessage(messages, messages.getStandardMessages().notEmpty()));
		}
		return null;
	}

}
