package org.bundolo.client.event.list;

import com.google.gwt.event.shared.EventHandler;

public interface ItemListDeletedEventHandler extends EventHandler {
  void onItemListDeleted(ItemListDeletedEvent event);
}
