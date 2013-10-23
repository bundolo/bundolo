package org.bundolo.client.event.text;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditTextEvent extends GwtEvent<EditTextEventHandler>{
  public static Type<EditTextEventHandler> TYPE = new Type<EditTextEventHandler>();
  private final ContentDTO contentDTO;
  
  public EditTextEvent(ContentDTO contentDTO) {
    this.contentDTO = contentDTO;
  }
  
  public ContentDTO getContentDTO() {
	return contentDTO;
}

@Override
  public Type<EditTextEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(EditTextEventHandler handler) {
    handler.onEditText(this);
  }
}
