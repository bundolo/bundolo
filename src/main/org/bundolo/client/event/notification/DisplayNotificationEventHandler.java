package org.bundolo.client.event.notification;

import com.google.gwt.event.shared.EventHandler;

public interface DisplayNotificationEventHandler extends EventHandler{
  void onDisplayNotification(DisplayNotificationEvent event);
}
