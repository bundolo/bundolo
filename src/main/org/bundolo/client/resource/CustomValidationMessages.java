package org.bundolo.client.resource;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.model.enumeration.LabelType;

import eu.maydu.gwt.validation.client.i18n.StandardValidationMessages;
import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

public class CustomValidationMessages extends ValidationMessages {

	public CustomValidationMessages(StandardValidationMessages messages) {
		super(messages);
	}

	@Override
	public String getPropertyName(String propertyName) {
		return LocalStorage.getInstance().getMessageResource().getLabel(LabelType.valueOf(propertyName));
	}

	@Override
	public String getCustomMessage(String key, Object... parameters) {
		return LocalStorage.getInstance().getMessageResource().getLabel(LabelType.valueOf(key), parameters);
	}

}