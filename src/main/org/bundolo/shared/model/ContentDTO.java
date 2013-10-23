package org.bundolo.shared.model;

import java.util.Date;
import java.util.List;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.view.client.ProvidesKey;

public class ContentDTO implements java.io.Serializable {

	private static final long serialVersionUID = -3335321743639936087L;

	private Long contentId;
	
	private String authorUsername;
	
	private Long parentContentId;
	
	private ContentKindType kind;
	
	private String name;
	
	private String text;
	
	private String locale;
	
	private Date creationDate;
	
	private ContentStatusType contentStatus;
	
	private List<ContentDTO> contentChildren;
	
	private RatingDTO rating;
	
	private ContentDTO descriptionContent;

	public ContentDTO() {
		super();
	}
	
	public ContentDTO(Long contentId, String authorUsername, Long parentContentId, ContentKindType kind, String name, String text, String locale,
			Date creationDate, ContentStatusType contentStatus) {
		super();
		this.contentId = contentId;
		this.authorUsername = authorUsername;
		this.parentContentId = parentContentId;
		this.kind = kind;
		this.name = name;
		this.text = text;
		this.locale = locale;
		this.creationDate = creationDate;
		this.contentStatus = contentStatus;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
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

	public Long getParentContentId() {
		return parentContentId;
	}

	public void setParentContentId(Long parentContentId) {
		this.parentContentId = parentContentId;
	}

	public ContentKindType getKind() {
		return kind;
	}

	public void setKind(ContentKindType kind) {
		this.kind = kind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public ContentStatusType getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(ContentStatusType contentStatus) {
		this.contentStatus = contentStatus;
	}

	public List<ContentDTO> getContentChildren() {
		return contentChildren;
	}

	public void setContentChildren(List<ContentDTO> contentChildren) {
		this.contentChildren = contentChildren;
	}

    public RatingDTO getRating() {
		return rating;
	}

	public void setRating(RatingDTO rating) {
		this.rating = rating;
	}

	public static final ProvidesKey<ContentDTO> KEY_PROVIDER = new ProvidesKey<ContentDTO>() {
      @Override
      public Object getKey(ContentDTO item) {
        return item == null ? null : item.getContentId();
      }
    };

	public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}
	
}