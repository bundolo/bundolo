package org.bundolo.shared.model;

import java.util.Date;

import org.bundolo.shared.model.enumeration.UserProfileGenderType;

import com.google.gwt.view.client.ProvidesKey;

public class UserDTO implements java.io.Serializable {

	private static final long serialVersionUID = -4042511031705727688L;
	
	private String  firstName;
	
	private String  lastName;
	
	private UserProfileGenderType  gender;
	
	private String username;
	
	private Boolean showPersonal;
	
	private Date birthDate;
	
	private Date signupDate;
	
	private Date lastLoginDate;
	
	private String avatarUrl;
	
	private String sessionId;
	
	private String rememberMeCookie;
	
	private ContentDTO descriptionContent;

	public UserDTO() {
		super();
	}
	
	public UserDTO(String firstName, String lastName, UserProfileGenderType gender,
			String username, Boolean showPersonal,
			Date birthDate, Date signupDate, Date lastLoginDate, String avatarUrl, String sessionId, String rememberMeCookie) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.username = username;
		this.showPersonal = showPersonal;
		this.birthDate = birthDate;
		this.signupDate = signupDate;
		this.lastLoginDate = lastLoginDate;
		this.avatarUrl = avatarUrl;
		this.sessionId = sessionId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserProfileGenderType getGender() {
		return gender;
	}

	public void setGender(UserProfileGenderType gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getShowPersonal() {
		return showPersonal;
	}

	public void setShowPersonal(Boolean showPersonal) {
		this.showPersonal = showPersonal;
	}

	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRememberMeCookie() {
		return rememberMeCookie;
	}

	public void setRememberMeCookie(String rememberMeCookie) {
		this.rememberMeCookie = rememberMeCookie;
	}

    public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}

	public static final ProvidesKey<UserDTO> KEY_PROVIDER = new ProvidesKey<UserDTO>() {
      @Override
      public Object getKey(UserDTO item) {
        return item == null ? null : item.getUsername();
      }
    };
}
