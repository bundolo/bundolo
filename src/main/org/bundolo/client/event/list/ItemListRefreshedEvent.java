package org.bundolo.client.event.list;

import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ItemListNameType;

import com.google.gwt.event.shared.GwtEvent;

public class ItemListRefreshedEvent extends GwtEvent<ItemListRefreshedEventHandler>{
	public static Type<ItemListRefreshedEventHandler> TYPE = new Type<ItemListRefreshedEventHandler>();
	private final ItemListNameType refreshedItemListName;

	public ItemListRefreshedEvent(ItemListNameType refreshedItemListName) {
		this.refreshedItemListName = refreshedItemListName;
	}

	public ItemListNameType getRefreshedItemListName() {
		return refreshedItemListName;
	}

	@Override
	public Type<ItemListRefreshedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ItemListRefreshedEventHandler handler) {
		handler.onItemListRefreshed(this);
	}
}