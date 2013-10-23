package org.bundolo.client.widget;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedHTMLPanel extends HTMLPanel {

	public ExtendedHTMLPanel(String html) {
		super(html);
	}

	@Override
	public void insert(Widget child, Element container, int beforeIndex, boolean domInsert) {
		super.insert(child, container, beforeIndex, domInsert);
	}

}
