package org.bundolo.client.event.user;

import com.google.gwt.event.shared.EventHandler;

public interface MessageSentEventHandler extends EventHandler {
  void onMessageSent(MessageSentEvent event);
}
