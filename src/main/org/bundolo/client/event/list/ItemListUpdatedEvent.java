package org.bundolo.client.event.list;

import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.event.shared.GwtEvent;

public class ItemListUpdatedEvent extends GwtEvent<ItemListUpdatedEventHandler>{
	public static Type<ItemListUpdatedEventHandler> TYPE = new Type<ItemListUpdatedEventHandler>();
	private final ItemListDTO updatedItemList;

	public ItemListUpdatedEvent(ItemListDTO updatedItemList) {
		this.updatedItemList = updatedItemList;
	}

	public ItemListDTO getUpdatedItemList() {
		return updatedItemList;
	}

	@Override
	public Type<ItemListUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ItemListUpdatedEventHandler handler) {
		handler.onItemListUpdated(this);
	}
}