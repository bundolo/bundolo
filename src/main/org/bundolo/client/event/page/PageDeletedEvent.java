package org.bundolo.client.event.page;

import com.google.gwt.event.shared.GwtEvent;

public class PageDeletedEvent extends GwtEvent<PageDeletedEventHandler>{
  public static Type<PageDeletedEventHandler> TYPE = new Type<PageDeletedEventHandler>();
  
  @Override
  public Type<PageDeletedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(PageDeletedEventHandler handler) {
    handler.onPageDeleted(this);
  }
}
