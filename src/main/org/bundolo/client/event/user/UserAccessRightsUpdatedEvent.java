package org.bundolo.client.event.user;

import com.google.gwt.event.shared.GwtEvent;

public class UserAccessRightsUpdatedEvent extends GwtEvent<UserAccessRightsUpdatedEventHandler>{
	public static Type<UserAccessRightsUpdatedEventHandler> TYPE = new Type<UserAccessRightsUpdatedEventHandler>();

	@Override
	public Type<UserAccessRightsUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserAccessRightsUpdatedEventHandler handler) {
		handler.onUserAccessRightsUpdated(this);
	}
}