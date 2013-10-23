package org.bundolo.client.event.list;

import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditItemListEvent extends GwtEvent<EditItemListEventHandler>{
	public static Type<EditItemListEventHandler> TYPE = new Type<EditItemListEventHandler>();
	private final ItemListDTO itemListDTO;

	public EditItemListEvent(ItemListDTO itemListDTO) {
		this.itemListDTO = itemListDTO;
	}

	public ItemListDTO getItemListDTO() {
		return itemListDTO;
	}

	@Override
	public Type<EditItemListEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditItemListEventHandler handler) {
		handler.onEditItemList(this);
	}
}
