package org.bundolo.client.event.list;

import com.google.gwt.event.shared.GwtEvent;

public class AddItemListEvent extends GwtEvent<AddItemListEventHandler> {
	public static Type<AddItemListEventHandler> TYPE = new Type<AddItemListEventHandler>();

	@Override
	public Type<AddItemListEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddItemListEventHandler handler) {
		handler.onAddItemList(this);
	} 
}
