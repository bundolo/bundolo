package org.bundolo.client.event.text;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class TextUpdatedEvent extends GwtEvent<TextUpdatedEventHandler>{
	public static Type<TextUpdatedEventHandler> TYPE = new Type<TextUpdatedEventHandler>();
	private final ContentDTO updatedContent;

	public TextUpdatedEvent(ContentDTO updatedContent) {
		this.updatedContent = updatedContent;
	}

	public ContentDTO getUpdatedContent() {
		return updatedContent;
	}

	@Override
	public Type<TextUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TextUpdatedEventHandler handler) {
		handler.onTextUpdated(this);
	}
}