package org.bundolo.client.event.page;

import org.bundolo.shared.model.PageDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditPageEvent extends GwtEvent<EditPageEventHandler>{
  public static Type<EditPageEventHandler> TYPE = new Type<EditPageEventHandler>();
  private final PageDTO pageDTO;
  
  public EditPageEvent(PageDTO pageDTO) {
    this.pageDTO = pageDTO;
  }
  
  public PageDTO getPageDTO() {
	return pageDTO;
}

@Override
  public Type<EditPageEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditPageEventHandler handler) {
    handler.onEditPage(this);
  }
}
