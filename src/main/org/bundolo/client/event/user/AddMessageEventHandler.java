package org.bundolo.client.event.user;

import com.google.gwt.event.shared.EventHandler;

public interface AddMessageEventHandler extends EventHandler {
  void onAddMessage(AddMessageEvent event);
}
