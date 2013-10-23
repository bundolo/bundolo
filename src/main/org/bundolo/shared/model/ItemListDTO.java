package org.bundolo.shared.model;

import java.util.Date;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.ItemListKindType;
import org.bundolo.shared.model.enumeration.ItemListStatusType;
import org.bundolo.shared.model.enumeration.LabelType;

public class ItemListDTO implements java.io.Serializable {

	private static final long serialVersionUID = 3166313558576648350L;

	private Long itemListId;
	
	private String authorUsername;
	
	private ItemListStatusType itemListStatus;
	
	private ItemListKindType kind;
	
	private Date creationDate;
	
	private String query;
	
	private Long descriptionContentId;
	
	private ContentDTO descriptionContent;

	public ItemListDTO() {
		super();
	}
	
	public ItemListDTO(ItemListDTO itemListDTO) {
		this();
		if (itemListDTO != null) {
			this.itemListId = itemListDTO.getItemListId();
			this.authorUsername = itemListDTO.getAuthorUsername();
			this.itemListStatus = itemListDTO.getItemListStatus();
			this.kind = itemListDTO.getKind();
			this.creationDate = itemListDTO.getCreationDate();
			this.query = itemListDTO.getQuery();
			this.descriptionContentId = itemListDTO.getDescriptionContentId();
			this.descriptionContent = itemListDTO.getDescriptionContent();
		}
	}

	public ItemListDTO(Long itemListId, String authorUsername, ItemListStatusType itemListStatus, ItemListKindType kind, Date creationDate, String query,
			Long descriptionContentId) {
		super();
		this.itemListId = itemListId;
		this.authorUsername = authorUsername;
		this.itemListStatus = itemListStatus;
		this.kind = kind;
		this.creationDate = creationDate;
		this.query = query;
		this.descriptionContentId = descriptionContentId;
	}

	public Long getItemListId() {
		return itemListId;
	}

	public void setItemListId(Long itemListId) {
		this.itemListId = itemListId;
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

	public ItemListStatusType getItemListStatus() {
		return itemListStatus;
	}

	public void setItemListStatus(ItemListStatusType itemListStatus) {
		this.itemListStatus = itemListStatus;
	}

	public ItemListKindType getKind() {
		return kind;
	}

	public void setKind(ItemListKindType kind) {
		this.kind = kind;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Long getDescriptionContentId() {
		return descriptionContentId;
	}

	public void setDescriptionContentId(Long descriptionContentId) {
		this.descriptionContentId = descriptionContentId;
	}

	public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}

}
