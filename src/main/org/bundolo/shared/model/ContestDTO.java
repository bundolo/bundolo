package org.bundolo.shared.model;

import java.util.Date;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.ContestKindType;
import org.bundolo.shared.model.enumeration.ContestStatusType;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.view.client.ProvidesKey;

public class ContestDTO implements java.io.Serializable {

	private static final long serialVersionUID = 7180068362964603625L;

	private Long contestId;

	private String authorUsername;

	private Long descriptionContentId;

	private ContestKindType kind;

	private Date creationDate;

	private Date expirationDate;

	private ContestStatusType contestStatus;

	private ContentDTO descriptionContent;

	public ContestDTO() {
		super();
	}

	public ContestDTO(Long contestId, String authorUsername, Long descriptionContentId, ContestKindType kind, Date creationDate, Date expirationDate,
			ContestStatusType contestStatus, ContentDTO descriptionContent) {
		super();
		this.contestId = contestId;
		this.authorUsername = authorUsername;
		this.descriptionContentId = descriptionContentId;
		this.kind = kind;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
		this.contestStatus = contestStatus;
		this.descriptionContent = descriptionContent;
	}

	public Long getContestId() {
		return contestId;
	}

	public void setContestId(Long contestId) {
		this.contestId = contestId;
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

	public Long getDescriptionContentId() {
		return descriptionContentId;
	}

	public void setDescriptionContentId(Long descriptionContentId) {
		this.descriptionContentId = descriptionContentId;
	}

	public ContestKindType getKind() {
		return kind;
	}

	public void setKind(ContestKindType kind) {
		this.kind = kind;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public ContestStatusType getContestStatus() {
		return contestStatus;
	}

	public void setContestStatus(ContestStatusType contestStatus) {
		this.contestStatus = contestStatus;
	}

	public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}

	public static final ProvidesKey<ContestDTO> KEY_PROVIDER = new ProvidesKey<ContestDTO>() {
		@Override
		public Object getKey(ContestDTO item) {
			return item == null ? null : item.getContestId();
		}
	};
}