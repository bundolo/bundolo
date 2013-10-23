package org.bundolo.client.event.connection;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionUpdatedEventHandler extends EventHandler{
  void onConnectionUpdated(ConnectionUpdatedEvent event);
}
