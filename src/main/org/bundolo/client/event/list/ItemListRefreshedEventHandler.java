package org.bundolo.client.event.list;

import com.google.gwt.event.shared.EventHandler;

public interface ItemListRefreshedEventHandler extends EventHandler{
  void onItemListRefreshed(ItemListRefreshedEvent event);
}
