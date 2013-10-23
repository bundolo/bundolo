package org.bundolo.client.event.connection;

import com.google.gwt.event.shared.EventHandler;

public interface AddConnectionEventHandler extends EventHandler {
  void onAddConnection(AddConnectionEvent event);
}
