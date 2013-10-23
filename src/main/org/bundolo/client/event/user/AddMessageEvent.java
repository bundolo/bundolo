package org.bundolo.client.event.user;

import org.bundolo.shared.model.UserDTO;

import com.google.gwt.event.shared.GwtEvent;

public class AddMessageEvent extends GwtEvent<AddMessageEventHandler> {
	public static Type<AddMessageEventHandler> TYPE = new Type<AddMessageEventHandler>();
	
	private final UserDTO recipientUserDTO;

	public AddMessageEvent(UserDTO recipientUserDTO) {
		this.recipientUserDTO = recipientUserDTO;
	}

	public UserDTO getRecipientUserDTO() {
		return recipientUserDTO;
	}

	@Override
	public Type<AddMessageEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddMessageEventHandler handler) {
		handler.onAddMessage(this);
	} 
}
