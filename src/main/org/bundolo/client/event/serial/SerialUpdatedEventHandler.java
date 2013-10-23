package org.bundolo.client.event.serial;

import com.google.gwt.event.shared.EventHandler;

public interface SerialUpdatedEventHandler extends EventHandler{
  void onSerialUpdated(SerialUpdatedEvent event);
}
