package org.bundolo.shared.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bundolo.client.LocalStorage;
import org.bundolo.server.model.Content;
import org.bundolo.server.model.Page;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageKindType;
import org.bundolo.shared.model.enumeration.PageStatusType;

public class PageDTO implements java.io.Serializable {

	private static final long serialVersionUID = 4789175881499639634L;
	
	private Long pageId;
	
	private String authorUsername;
	
	private Long parentPageId;
	
	private Integer displayOrder;
	
	private PageStatusType pageStatus;
	
	private PageKindType kind;
	
	private Date creationDate;
	
	private ContentDTO descriptionContent;

	public PageDTO() {
		super();
	}

	public PageDTO(Long pageId, String authorUsername, Long parentPageId, Integer displayOrder, PageStatusType pageStatus, PageKindType kind,
			Date creationDate) {
		super();
		this.pageId = pageId;
		this.authorUsername = authorUsername;
		this.parentPageId = parentPageId;
		this.displayOrder = displayOrder;
		this.pageStatus = pageStatus;
		this.kind = kind;
		this.creationDate = creationDate;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

	public String getAuthorDisplayName() {
		if (authorUsername == null || Constants.OLD_DB_GUEST_USERNAME.equals(authorUsername)) {
			return LocalStorage.getInstance().getMessageResource().getLabel(LabelType.anonymous_user);
		} else {
			return authorUsername;
		}
	}

	public Long getParentPageId() {
		return parentPageId;
	}

	public void setParentPageId(Long parentPageId) {
		this.parentPageId = parentPageId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public PageStatusType getPageStatus() {
		return pageStatus;
	}

	public void setPageStatus(PageStatusType pageStatus) {
		this.pageStatus = pageStatus;
	}

	public PageKindType getKind() {
		return kind;
	}

	public void setKind(PageKindType kind) {
		this.kind = kind;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}
}
