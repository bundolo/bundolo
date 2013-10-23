package org.bundolo.client.event.user;

import org.bundolo.shared.model.UserProfileDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditUserProfileEvent extends GwtEvent<EditUserProfileEventHandler>{
	public static Type<EditUserProfileEventHandler> TYPE = new Type<EditUserProfileEventHandler>();
	private final UserProfileDTO userProfileDTO;

	public EditUserProfileEvent(UserProfileDTO userProfileDTO) {
		this.userProfileDTO = userProfileDTO;
	}
	
	public UserProfileDTO getUserProfileDTO() {
		return userProfileDTO;
	}

	@Override
	public Type<EditUserProfileEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditUserProfileEventHandler handler) {
		handler.onEditUserProfile(this);
	}
}
