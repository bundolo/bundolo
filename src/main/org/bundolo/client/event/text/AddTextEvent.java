package org.bundolo.client.event.text;

import com.google.gwt.event.shared.GwtEvent;

public class AddTextEvent extends GwtEvent<AddTextEventHandler> {
	public static Type<AddTextEventHandler> TYPE = new Type<AddTextEventHandler>();

	@Override
	public Type<AddTextEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddTextEventHandler handler) {
		handler.onAddText(this);
	} 
}
