package org.bundolo.client.event.text;

import com.google.gwt.event.shared.EventHandler;

public interface TextUpdatedEventHandler extends EventHandler{
  void onTextUpdated(TextUpdatedEvent event);
}
