package org.bundolo.client.event.user;

import com.google.gwt.event.shared.GwtEvent;

public class UserProfileDeletedEvent extends GwtEvent<UserProfileDeletedEventHandler>{
  public static Type<UserProfileDeletedEventHandler> TYPE = new Type<UserProfileDeletedEventHandler>();
  
  @Override
  public Type<UserProfileDeletedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(UserProfileDeletedEventHandler handler) {
    handler.onUserProfileDeleted(this);
  }
}
