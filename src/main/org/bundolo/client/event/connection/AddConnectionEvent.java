package org.bundolo.client.event.connection;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class AddConnectionEvent extends GwtEvent<AddConnectionEventHandler> {
	public static Type<AddConnectionEventHandler> TYPE = new Type<AddConnectionEventHandler>();
	
	private final ContentDTO parentContentDTO;

	public AddConnectionEvent(ContentDTO parentContentDTO) {
		this.parentContentDTO = parentContentDTO;
	}

	public ContentDTO getParentContentDTO() {
		return parentContentDTO;
	}

	@Override
	public Type<AddConnectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddConnectionEventHandler handler) {
		handler.onAddConnection(this);
	} 
}
