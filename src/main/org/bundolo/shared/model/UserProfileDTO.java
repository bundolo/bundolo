package org.bundolo.shared.model;

import java.util.Date;

import org.bundolo.shared.model.enumeration.UserProfileGenderType;
import org.bundolo.shared.model.enumeration.UserProfileStatusType;

public class UserProfileDTO implements java.io.Serializable {

	private static final long serialVersionUID = -4042511031705727688L;
	
	private Long userId;
	
	private String username;
	
	private String  password;
	
	private String  salt;
	
	private String  firstName;
	
	private String  lastName;
	
	private UserProfileGenderType  gender;
	
	private String email;
	
	private Boolean showPersonal;
	
	private Date birthDate;
	
	private Date signupDate;
	
	private Date lastLoginDate;
	
	private String lastIp;
	
	private UserProfileStatusType userProfileStatus;
	
	private String avatarUrl;
	
	private String sessionId;
	
	private String nonce;
	
	private String newEmail;
	
	private ContentDTO descriptionContent;

	public UserProfileDTO() {
		super();
	}
	
	public UserProfileDTO(Long userId, String username, String password, String salt,
			String firstName, String lastName, UserProfileGenderType gender, String email, Boolean showPersonal,
			Date birthDate, Date signupDate, Date lastLoginDate, String lastIp,
			UserProfileStatusType userProfileStatus, String avatarUrl, String sessionId, String nonce, String newEmail) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.email = email;
		this.showPersonal = showPersonal;
		this.birthDate = birthDate;
		this.signupDate = signupDate;
		this.lastLoginDate = lastLoginDate;
		this.lastIp = lastIp;
		this.userProfileStatus = userProfileStatus;
		this.avatarUrl = avatarUrl;
		this.sessionId = sessionId;
		this.nonce = nonce;
		this.newEmail = newEmail;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public UserProfileStatusType getUserProfileStatus() {
		return userProfileStatus;
	}

	public void setUserProfileStatus(UserProfileStatusType userProfileStatus) {
		this.userProfileStatus = userProfileStatus;
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

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public ContentDTO getDescriptionContent() {
		return descriptionContent;
	}

	public void setDescriptionContent(ContentDTO descriptionContent) {
		this.descriptionContent = descriptionContent;
	}
}
