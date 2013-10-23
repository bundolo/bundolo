package org.bundolo.shared.model;

import java.util.Date;

import org.bundolo.client.LocalStorage;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.enumeration.ConnectionKindType;
import org.bundolo.shared.model.enumeration.ConnectionStatusType;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.view.client.ProvidesKey;

public class ConnectionDTO implements java.io.Serializable {

	private static final long serialVersionUID = 8621630193911251244L;

	private Long connectionId;

	private String authorUsername;

	private Long parentContentId;

	private Long descriptionContentId;

	private ConnectionKindType kind;

	private Date creationDate;

	private ConnectionStatusType connectionStatus;

	private String email;

	private String url;

	private ContentDTO descriptionContent;

	public ConnectionDTO() {
		super();
	}

	public ConnectionDTO(Long connectionId, String authorUsername, Long parentContentId, Long descriptionContentId, ConnectionKindType kind, Date creationDate,
			ConnectionStatusType connectionStatus, String email, String url, ContentDTO descriptionContent) {
		super();
		this.connectionId = connectionId;
		this.authorUsername = authorUsername;
		this.parentContentId = parentContentId;
		this.descriptionContentId = descriptionContentId;
		this.kind = kind;
		this.creationDate = creationDate;
		this.connectionStatus = connectionStatus;
		this.email = email;
		this.url = url;
		this.descriptionContent = descriptionContent;
	}

	public Long getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
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

	public Long getDescriptionContentId() {
		return descriptionContentId;
	}

	public void setDescriptionContentId(Long descriptionContentId) {
		this.descriptionContentId = descriptionContentId;
	}

	public ConnectionKindType getKind() {
		return kind;
	}

	public void setKind(ConnectionKindType kind) {
		this.kind = kind;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public ConnectionStatusType getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(ConnectionStatusType connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}

	public static final ProvidesKey<ConnectionDTO> KEY_PROVIDER = new ProvidesKey<ConnectionDTO>() {
		@Override
		public Object getKey(ConnectionDTO item) {
			return item == null ? null : item.getConnectionId();
		}
	};
}
