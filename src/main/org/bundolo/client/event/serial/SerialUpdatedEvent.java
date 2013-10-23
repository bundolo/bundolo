package org.bundolo.client.event.serial;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class SerialUpdatedEvent extends GwtEvent<SerialUpdatedEventHandler>{
	public static Type<SerialUpdatedEventHandler> TYPE = new Type<SerialUpdatedEventHandler>();
	private final ContentDTO updatedContent;

	public SerialUpdatedEvent(ContentDTO updatedContent) {
		this.updatedContent = updatedContent;
	}

	public ContentDTO getUpdatedContent() {
		return updatedContent;
	}

	@Override
	public Type<SerialUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SerialUpdatedEventHandler handler) {
		handler.onSerialUpdated(this);
	}
}