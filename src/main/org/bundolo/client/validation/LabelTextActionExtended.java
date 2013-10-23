/*
Copyright 2009 Anatol Gregory Mayen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/
package org.bundolo.client.validation;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;

import eu.maydu.gwt.validation.client.ValidationAction;
import eu.maydu.gwt.validation.client.ValidationResult;
import eu.maydu.gwt.validation.client.ValidationResult.ValidationError;


/**
* Action that sets the specified error message on the specified
* label.
* 
* @author Anatol Gregory Mayen
*
*/
public class LabelTextActionExtended extends ValidationAction<Object> {

	private String defaultLabelText = "";
	private Label errorLabel;
	private String delimiter;
	private boolean withPropertyName = true;
	private LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
	
	public LabelTextActionExtended(Label errorLabel, LabelType labelType) {
		this(errorLabel);
		defaultLabelText = LocalStorage.getInstance().getMessageResource().getLabel(labelType);
	}
	
	public LabelTextActionExtended(Label errorLabel) {
		this(errorLabel, ": ");
	}
	
	public LabelTextActionExtended(Label errorLabel, boolean printPropertyName) {
		this(errorLabel, ": ");
		this.withPropertyName = printPropertyName;
	}
	
	public LabelTextActionExtended(Label errorLabel, String delimiter) {
		this(errorLabel, delimiter, true);
	}
	
	public LabelTextActionExtended(Label errorLabel, String delimiter, boolean printPropertyName) {
		if(errorLabel == null)
			throw new IllegalArgumentException("errorLabel must not be null");
		if(delimiter == null)
			throw new IllegalArgumentException("delimiter must not be null");
		this.errorLabel = errorLabel;
		this.delimiter = delimiter;
		this.withPropertyName = printPropertyName;
	}
	
	@Override
	public void invoke(ValidationResult result, Object notUsed) {
		if(result == null )
			return;
		
		ValidationError error = result.getErrors().get(0);
		
		
		String prefix = "";
		if(withPropertyName && error.propertyName != null && !error.propertyName.trim().equals("")) {
			if(!localeInfo.isRTL())
				prefix = error.propertyName+delimiter;
			else
				prefix = delimiter+error.propertyName;
		}
		if(!localeInfo.isRTL())
			this.errorLabel.setText(prefix+error.error);
		else
			this.errorLabel.setText(error.error+prefix);
		
	}
	
	@Override
	public void reset(UIObject obj) {
		reset();
	}
	
	@Override
	public void reset() {
		this.errorLabel.setText(defaultLabelText);
	}

}