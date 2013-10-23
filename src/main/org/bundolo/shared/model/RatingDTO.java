package org.bundolo.shared.model;

import java.util.Date;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.RatingKindType;
import org.bundolo.shared.model.enumeration.RatingStatusType;

import com.google.gwt.view.client.ProvidesKey;

public class RatingDTO implements java.io.Serializable {

	private static final long serialVersionUID = 7180068362964603625L;

	private Long ratingId;
	
	private String authorUsername;
	
	private Long parentContentId;
	
	private RatingKindType kind;
	
	private Date creationDate;
	
	private RatingStatusType ratingStatus;
	
	private Long value;	

	public RatingDTO() {
		super();
	}

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
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

	public RatingKindType getKind() {
		return kind;
	}

	public void setKind(RatingKindType kind) {
		this.kind = kind;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public RatingStatusType getRatingStatus() {
		return ratingStatus;
	}

	public void setRatingStatus(RatingStatusType ratingStatus) {
		this.ratingStatus = ratingStatus;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public RatingDTO(Long ratingId, String authorUsername, Long parentContentId, RatingKindType kind, Date creationDate, RatingStatusType ratingStatus,
			Long value) {
		super();
		this.ratingId = ratingId;
		this.authorUsername = authorUsername;
		this.parentContentId = parentContentId;
		this.kind = kind;
		this.creationDate = creationDate;
		this.ratingStatus = ratingStatus;
		this.value = value;
	}

	public static final ProvidesKey<RatingDTO> KEY_PROVIDER = new ProvidesKey<RatingDTO>() {
		@Override
		public Object getKey(RatingDTO item) {
			return item == null ? null : item.getRatingId();
		}
	};
}