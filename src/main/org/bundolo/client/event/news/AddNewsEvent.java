package org.bundolo.client.event.news;

import com.google.gwt.event.shared.GwtEvent;

public class AddNewsEvent extends GwtEvent<AddNewsEventHandler> {
	public static Type<AddNewsEventHandler> TYPE = new Type<AddNewsEventHandler>();

	@Override
	public Type<AddNewsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddNewsEventHandler handler) {
		handler.onAddNews(this);
	} 
}
