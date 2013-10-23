package org.bundolo.client.event.page;

import org.bundolo.shared.model.PageDTO;

import com.google.gwt.event.shared.GwtEvent;

public class AddPageEvent extends GwtEvent<AddPageEventHandler> {
  public static Type<AddPageEventHandler> TYPE = new Type<AddPageEventHandler>();
  
  private final PageDTO parentPageDTO;
  
  public AddPageEvent(PageDTO parentPageDTO) {
	this.parentPageDTO = parentPageDTO;
}
  
  public PageDTO getParentPageDTO() {
		return parentPageDTO;
	}

@Override
  public Type<AddPageEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AddPageEventHandler handler) {
    handler.onAddPage(this);
  }
}
