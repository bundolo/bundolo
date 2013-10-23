package org.bundolo.client.event.user;

import com.google.gwt.event.shared.EventHandler;

public interface UserProfileUpdatedEventHandler extends EventHandler{
  void onUserProfileUpdated(UserProfileUpdatedEvent event);
}
