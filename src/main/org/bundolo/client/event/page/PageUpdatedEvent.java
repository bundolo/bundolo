package org.bundolo.client.event.page;

import org.bundolo.shared.model.PageDTO;

import com.google.gwt.event.shared.GwtEvent;

public class PageUpdatedEvent extends GwtEvent<PageUpdatedEventHandler>{
  public static Type<PageUpdatedEventHandler> TYPE = new Type<PageUpdatedEventHandler>();
  private final PageDTO updatedPage;
  
  public PageUpdatedEvent(PageDTO updatedPage) {
    this.updatedPage = updatedPage;
  }
  
  public PageDTO getUpdatedContact() { return updatedPage; }
  

  @Override
  public Type<PageUpdatedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(PageUpdatedEventHandler handler) {
    handler.onPageUpdated(this);
  }
}
