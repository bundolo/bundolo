package org.bundolo.shared.services;

import java.util.List;
import java.util.Map;

import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bundoloServices/contentService")
public interface ContentService extends RemoteService {
	
	public ContentDTO findContent(Long contentId) throws Exception;
//	public void saveContent(Long contentId, Long authorUserId, Long parentPageId, Long parentContentId, ContentKindType kind, String text, String locale, Date creationDate, ContentStatusType contentStatus) throws Exception;
//	public void updateContent(Long contentId, Long authorUserId, Long parentPageId, Long parentContentId, ContentKindType kind, String text, String locale, Date creationDate, ContentStatusType contentStatus) throws Exception;
//	public void saveOrUpdateContent(Long contentId, Long authorUserId, Long parentPageId, Long parentContentId, ContentKindType kind, String text, String locale, Date creationDate, ContentStatusType contentStatus) throws Exception;
	public Long saveContent(ContentDTO contentDTO) throws Exception;
	public void updateContent(ContentDTO contentDTO) throws Exception;
	//public void saveOrUpdateContent(ContentDTO contentDTO) throws Exception;
	public void deleteContent(Long contentId) throws Exception;
	/**
	 * Get content comments.
	 * 
	 * @param parentContentId
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	public List<ContentDTO> findContents(Long parentContentId, ContentKindType kind) throws Exception;
	public Map<String, String> getLabelsForLocale(String locale) throws Exception;
	public List<ContentDTO> findItemListContents(String query, Integer start, Integer end) throws Exception;
	public Integer findItemListContentsCount(String query) throws Exception;
	public ContentDTO getDescriptionContent(Long parentContentId, ContentKindType kind) throws Exception;
	public void saveLabels(Map<String, String> labels) throws Exception;
}