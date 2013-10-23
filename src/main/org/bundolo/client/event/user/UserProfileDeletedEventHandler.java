package org.bundolo.client.event.user;

import com.google.gwt.event.shared.EventHandler;

public interface UserProfileDeletedEventHandler extends EventHandler {
  void onUserProfileDeleted(UserProfileDeletedEvent event);
}
