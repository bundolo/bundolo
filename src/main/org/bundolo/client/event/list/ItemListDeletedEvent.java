package org.bundolo.client.event.list;

import com.google.gwt.event.shared.GwtEvent;

public class ItemListDeletedEvent extends GwtEvent<ItemListDeletedEventHandler>{
  public static Type<ItemListDeletedEventHandler> TYPE = new Type<ItemListDeletedEventHandler>();
  
  @Override
  public Type<ItemListDeletedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(ItemListDeletedEventHandler handler) {
    handler.onItemListDeleted(this);
  }
}
