package org.bundolo.client.event.user;

import com.google.gwt.event.shared.GwtEvent;

public class AddUserProfileEvent extends GwtEvent<AddUserProfileEventHandler> {
	public static Type<AddUserProfileEventHandler> TYPE = new Type<AddUserProfileEventHandler>();

	@Override
	public Type<AddUserProfileEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddUserProfileEventHandler handler) {
		handler.onAddUserProfile(this);
	} 
}
