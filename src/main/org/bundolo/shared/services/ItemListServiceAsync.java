package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ItemListServiceAsync {

	void deleteItemList(Long itemListId, AsyncCallback<Void> callback);

	void findItemList(Long itemListId, AsyncCallback<ItemListDTO> callback);
	
	void saveItemList(ItemListDTO itemListDTO, AsyncCallback<Void> callback);

	//void saveOrUpdateItemList(ItemListDTO itemListDTO, AsyncCallback<Void> callback);

	void updateItemList(ItemListDTO itemListDTO, AsyncCallback<Void> callback);
	
	//void findAllItemLists(AsyncCallback<List<ItemListDTO>> callback);
	
//	void findItemListByName(String itemListName, Long authorUserId, AsyncCallback<ItemListDTO> callback);
//	
//	void findItemListByName(String itemListName, Long authorUserId, List<String> params, AsyncCallback<ItemListDTO> callback);
	
	void findItemList(Long itemListId, List<String> params, AsyncCallback<ItemListDTO> callback);

	void findItemListByName(String itemListName, String authorUsername, AsyncCallback<ItemListDTO> callback);

	void findItemListByName(String itemListName, String authorUsername, List<String> params, AsyncCallback<ItemListDTO> callback);

}
