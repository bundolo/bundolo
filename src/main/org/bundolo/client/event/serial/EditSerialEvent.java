package org.bundolo.client.event.serial;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditSerialEvent extends GwtEvent<EditSerialEventHandler>{
	public static Type<EditSerialEventHandler> TYPE = new Type<EditSerialEventHandler>();
	private final ContentDTO contentDTO;

	public EditSerialEvent(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public Type<EditSerialEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditSerialEventHandler handler) {
		handler.onEditSerial(this);
	}
}
