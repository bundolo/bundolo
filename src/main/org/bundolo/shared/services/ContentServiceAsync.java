package org.bundolo.shared.services;

import java.util.List;
import java.util.Map;

import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContentServiceAsync {

    void deleteContent(Long contentId, AsyncCallback<Void> callback);

    void findContent(Long contentId, AsyncCallback<ContentDTO> callback);

    // void saveContent(Long contentId, Long authorUserId, Long parentPageId,
    // Long parentContentId, ContentKindType kind, String text, String locale,
    // Date creationDate, ContentStatusType contentStatus, AsyncCallback<Void>
    // callback);
    //
    // void saveOrUpdateContent(Long contentId, Long authorUserId, Long
    // parentPageId, Long parentContentId, ContentKindType kind, String text,
    // String locale, Date creationDate, ContentStatusType contentStatus,
    // AsyncCallback<Void> callback);
    //
    // void updateContent(Long contentId, Long authorUserId, Long parentPageId,
    // Long parentContentId, ContentKindType kind, String text, String locale,
    // Date creationDate, ContentStatusType contentStatus, AsyncCallback<Void>
    // callback);

    void saveContent(ContentDTO contentDTO, AsyncCallback<Long> callback);

    // void saveOrUpdateContent(ContentDTO contentDTO, AsyncCallback<Void>
    // callback);

    void updateContent(ContentDTO contentDTO, AsyncCallback<Void> callback);

    void findContents(Long parentContentId, ContentKindType kind, AsyncCallback<List<ContentDTO>> callback);

    void getLabelsForLocale(String locale, AsyncCallback<Map<String, String>> callback);

    void findItemListContents(String query, Integer start, Integer end, AsyncCallback<List<ContentDTO>> callback);

    void findItemListContentsCount(String query, AsyncCallback<Integer> callback);

    void getDescriptionContent(Long parentContentId, ContentKindType kind, AsyncCallback<ContentDTO> callback);

    void saveLabels(Map<String, String> labels, AsyncCallback<Void> callback);
}
