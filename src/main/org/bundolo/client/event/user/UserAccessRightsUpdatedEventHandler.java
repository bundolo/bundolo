package org.bundolo.client.event.user;

import com.google.gwt.event.shared.EventHandler;

public interface UserAccessRightsUpdatedEventHandler extends EventHandler{
  void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event);
}
