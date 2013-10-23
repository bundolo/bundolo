package org.bundolo.client.resource;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.shared.Utils;
import org.bundolo.shared.model.enumeration.LabelType;

public class MessageResource {

	private static Logger logger = Logger.getLogger(MessageResource.class.getName());
	//private static MessageResource messageResource = null;

	private Map<String, String> localeLabels;

	public MessageResource() {
	}

	public void setLocaleLabels(Map<String, String> localeLabels) {
		//logger.severe("setLocaleLabels: " + localeLabels);
		this.localeLabels = localeLabels;
	}

//	public synchronized MessageResource getInstance() {
//		if (messageResource == null) {
//			messageResource = new MessageResource();
//		}
//		return messageResource;
//	}

	public String getLabel(LabelType labelType) {
		//logger.severe("getLabel: " + labelType);
		//logger.severe("getLabel result: " + localeLabels.get(labelType.getLabelId()));
		String result = localeLabels.get(labelType.getLabelName());
		if (result == null) {
			logger.log(Level.SEVERE, "Label " + labelType + " not found in database.");
			result = "&nbsp;";
		}
		return result;
	}
	
	public String getLabel(LabelType labelType, Object... params) {
		//logger.severe("getLabel: " + labelType);
		//logger.severe("getLabel result: " + localeLabels.get(labelType.getLabelId()));
		
		String result = localeLabels.get(labelType.getLabelName());
		if (result == null) {
			logger.log(Level.SEVERE, "Label " + labelType + " not found in database.");
			result = "&nbsp;";
		} else if (Utils.hasText(result) && params != null) {
			result = Utils.format(result, params);
		}
		return result;
	}
}