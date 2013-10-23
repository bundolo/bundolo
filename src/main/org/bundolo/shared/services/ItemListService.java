package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bundoloServices/itemListService")
public interface ItemListService extends RemoteService {
	
	public ItemListDTO findItemList(Long itemListId);
	public void saveItemList(ItemListDTO itemListDTO) throws Exception;
	public void updateItemList(ItemListDTO itemListDTO) throws Exception;
	//public void saveOrUpdateItemList(ItemListDTO itemListDTO) throws Exception;
	public void deleteItemList(Long itemListId) throws Exception;
	//public List<ItemListDTO> findAllItemLists() throws Exception; //TODO probably not needed
	public ItemListDTO findItemListByName(String itemListName, String authorUsername);
	public ItemListDTO findItemListByName(String itemListName, String authorUsername, List<String> params);
	
	public ItemListDTO findItemList(Long itemListId, List<String> params);
}
