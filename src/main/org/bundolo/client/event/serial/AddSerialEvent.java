package org.bundolo.client.event.serial;

import com.google.gwt.event.shared.GwtEvent;

public class AddSerialEvent extends GwtEvent<AddSerialEventHandler> {
	public static Type<AddSerialEventHandler> TYPE = new Type<AddSerialEventHandler>();

	@Override
	public Type<AddSerialEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddSerialEventHandler handler) {
		handler.onAddSerial(this);
	} 
}
