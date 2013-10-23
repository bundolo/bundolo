package org.bundolo.client.event.list;

import com.google.gwt.event.shared.EventHandler;

public interface ItemListUpdatedEventHandler extends EventHandler{
  void onItemListUpdated(ItemListUpdatedEvent event);
}
