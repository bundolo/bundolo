package org.bundolo.server.services;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bundolo.server.SessionUtils;
import org.bundolo.server.dao.ContentDAO;
import org.bundolo.server.dao.ItemListDAO;
import org.bundolo.server.model.Content;
import org.bundolo.server.model.ItemList;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.services.ItemListService;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("itemListService")
public class ItemListServiceImpl implements ItemListService {
	
	private static final Logger logger = Logger.getLogger(ItemListServiceImpl.class.getName());

	@Autowired
	private ItemListDAO itemListDAO;
	
	@Autowired
	private ContentDAO contentDAO;

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() {
	}

	@Override
	public ItemListDTO findItemList(Long itemListId) {
		ItemListDTO result = null;
		ItemList itemList = itemListDAO.findById(itemListId);
		if(itemList != null) {
			result = DozerBeanMapperSingletonWrapper.getInstance().map(itemList, ItemListDTO.class);
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void saveItemList(ItemListDTO itemListDTO) throws Exception {
		ItemList itemList = null;
		if (itemListDTO.getItemListId() != null) {
			itemList = itemListDAO.findById(itemListDTO.getItemListId());
		}
		//TODO saving description
		if(itemList == null) {
			itemList = new ItemList(itemListDTO.getItemListId(), SessionUtils.getUsername(), itemListDTO.getItemListStatus(), itemListDTO.getKind(), itemListDTO.getCreationDate(), itemListDTO.getQuery(), itemListDTO.getDescriptionContentId());
			try {
				itemListDAO.persist(itemList);
			} catch (Exception ex) {
				throw new Exception("db exception");
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateItemList(ItemListDTO itemListDTO) throws Exception {
		ItemList itemList = itemListDAO.findById(itemListDTO.getItemListId());

		if(itemList != null) {
			//itemList.setAuthorUsername(itemListDTO.getAuthorUsername());
			itemList.setItemListStatus(itemListDTO.getItemListStatus());
			itemList.setKind(itemListDTO.getKind());
			//itemList.setCreationDate(itemListDTO.getCreationDate());
			itemList.setQuery(itemListDTO.getQuery());
			//itemList.setDescriptionContentId(itemListDTO.getDescriptionContent().getContentId());
		}
		try {
			itemListDAO.merge(itemList);
		} catch (Exception ex) {
			throw new Exception("db exception");
		}
	}

//	@Override
//	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
//	public void saveOrUpdateItemList(ItemListDTO itemListDTO) throws Exception {
//		ItemList itemList = new ItemList(itemListDTO.getItemListId(), itemListDTO.getAuthorUsername(), itemListDTO.getItemListStatus(), itemListDTO.getKind(), itemListDTO.getCreationDate(), itemListDTO.getQuery(), itemListDTO.getDescriptionContent().getContentId());
//		itemListDAO.merge(itemList);
//	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteItemList(Long itemListId) throws Exception {
		ItemList itemList = itemListDAO.findById(itemListId);
		if(itemList != null)
			itemListDAO.remove(itemList);
	}

//	@Override
//	public List<ItemListDTO> findAllItemLists() throws Exception {
//		List<ItemList> itemLists = itemListDAO.findAll();
//		List<ItemListDTO> itemListDTOs = new ArrayList<ItemListDTO>();
//		if (itemLists != null) {
//			for (ItemList itemList : itemLists) {
//				itemListDTOs.add(DozerBeanMapperSingletonWrapper.getInstance().map(itemList, ItemListDTO.class));
//			}
//		}
//		return itemListDTOs;
//	}

	@Override
	public ItemListDTO findItemListByName(String itemListName, String authorUsername) {
		ItemListDTO result = null;
		ItemList itemList = itemListDAO.findByName(itemListName, authorUsername);
		if(itemList != null) {
			result = DozerBeanMapperSingletonWrapper.getInstance().map(itemList, ItemListDTO.class);
			Content descriptionContent = contentDAO.findContentForLocale(itemList.getDescriptionContentId(), ContentKindType.item_list_description, SessionUtils.getUserLocale());
			if (descriptionContent != null) {
				result.setDescriptionContent(DozerBeanMapperSingletonWrapper.getInstance().map(descriptionContent, ContentDTO.class));
			}
		}
		return result;
	}

	@Override
	public ItemListDTO findItemListByName(String itemListName, String authorUsername, List<String> params) {
		ItemListDTO result = findItemListByName(itemListName, authorUsername);
		if (result != null && Utils.hasText(result.getQuery()) && Utils.hasElements(params)) {
			String modifiedQuery = result.getQuery();
			for (int i = 0; i < params.size(); i++) {
				String param = params.get(i);
				modifiedQuery = modifiedQuery.replace("%" + i + "%", param);
			}
			result.setQuery(modifiedQuery);
		}
		return result;
	}

	@Override
	public ItemListDTO findItemList(Long itemListId, List<String> params) {
		ItemListDTO result = findItemList(itemListId);
		if (result != null && Utils.hasText(result.getQuery()) && Utils.hasElements(params)) {
			String modifiedQuery = result.getQuery();
			for (int i = 0; i < params.size(); i++) {
				String param = params.get(i);
				modifiedQuery = modifiedQuery.replace("%" + i + "%", param);
			}
			result.setQuery(modifiedQuery);
		}
		return result;
	}
}
