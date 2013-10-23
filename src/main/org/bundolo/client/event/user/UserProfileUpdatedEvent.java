package org.bundolo.client.event.user;

import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.UserProfileDTO;

import com.google.gwt.event.shared.GwtEvent;

public class UserProfileUpdatedEvent extends GwtEvent<UserProfileUpdatedEventHandler>{
	public static Type<UserProfileUpdatedEventHandler> TYPE = new Type<UserProfileUpdatedEventHandler>();
	private final UserDTO updatedUserProfile;

	public UserProfileUpdatedEvent(UserDTO updatedUserProfile) {
		this.updatedUserProfile = updatedUserProfile;
	}

	public UserDTO getUpdatedUserProfile() {
		return updatedUserProfile;
	}

	@Override
	public Type<UserProfileUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserProfileUpdatedEventHandler handler) {
		handler.onUserProfileUpdated(this);
	}
}