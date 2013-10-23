package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.PageDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PageServiceAsync {

	void deletePage(Long pageId, AsyncCallback<Void> callback);

	void findPage(Long pageId, AsyncCallback<PageDTO> callback);

//	void savePage(Long pageId, Long authorUserId, Long parentPageId, Integer displayOrder, PageStatusType pageStatus, PageKindType kind, String name, Date creationDate, AsyncCallback<Void> callback);
//
//	void saveOrUpdatePage(Long pageId, Long authorUserId, Long parentPageId, Integer displayOrder, PageStatusType pageStatus, PageKindType kind, String name, Date creationDate, AsyncCallback<Void> callback);
//
//	void updatePage(Long pageId, Long authorUserId, Long parentPageId, Integer displayOrder, PageStatusType pageStatus, PageKindType kind, String name, Date creationDate, AsyncCallback<Void> callback);
	
	void savePage(PageDTO pageDTO, AsyncCallback<Void> callback);

	//void saveOrUpdatePage(PageDTO pageDTO, AsyncCallback<Void> callback);

	void updatePage(PageDTO pageDTO, AsyncCallback<Void> callback);
	
	void updatePages(List<PageDTO> pageDTOs, AsyncCallback<Void> callback);
	
	//void findAllPages(AsyncCallback<List<PageDTO>> callback);
	
	void findChildPages(Long parentPageId, AsyncCallback<List<PageDTO>> callback);
	
	void findPagesCount(Long parentPageId, AsyncCallback<Integer> callback);
	
	//void findNextPage(Long previousPageId, AsyncCallback<PageDTO> callback);
	
	//void findHomePage(AsyncCallback<PageDTO> callback);
	
	void findNavigationPages(AsyncCallback<List<Object>> callback);

	void getAsciiArt(String text, AsyncCallback<String> callback);

}
