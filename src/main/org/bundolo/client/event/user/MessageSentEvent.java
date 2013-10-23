package org.bundolo.client.event.user;

import org.bundolo.shared.model.UserDTO;

import com.google.gwt.event.shared.GwtEvent;

public class MessageSentEvent extends GwtEvent<MessageSentEventHandler> {
	public static Type<MessageSentEventHandler> TYPE = new Type<MessageSentEventHandler>();
	
	private final UserDTO recipientUserDTO;

	public MessageSentEvent(UserDTO recipientUserDTO) {
		this.recipientUserDTO = recipientUserDTO;
	}

	public UserDTO getRecipientUserDTO() {
		return recipientUserDTO;
	}

	@Override
	public Type<MessageSentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MessageSentEventHandler handler) {
		handler.onMessageSent(this);
	} 
}
