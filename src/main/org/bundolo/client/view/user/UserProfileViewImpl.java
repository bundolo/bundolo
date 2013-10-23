package org.bundolo.client.view.user;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.presenter.LoginPresenter;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.client.widget.ConditionalPanel;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class UserProfileViewImpl extends Composite implements UserProfileView {

	private static final Logger logger = Logger.getLogger(UserProfileViewImpl.class.getName());

	@UiTemplate("UserProfileView.ui.xml")
	interface UserProfileViewUiBinder extends UiBinder<Widget, UserProfileViewImpl> {}
	private static UserProfileViewUiBinder uiBinder = GWT.create(UserProfileViewUiBinder.class);

	@UiField ConditionalPanel adminPanel;
	@UiField ConditionalPanel personalPanel;
	@UiField Label displayName;
	@UiField Label signupDate;
	@UiField Label firstName;
	@UiField Label lastName;
	@UiField Label birthDate;
	@UiField Label gender;
	//@UiField Label email;
	@UiField Label displayNameLabel;
	@UiField Label signupDateLabel;
	@UiField Label firstNameLabel;
	@UiField Label lastNameLabel;
	@UiField Label birthDateLabel;
	@UiField Label genderLabel;
	//@UiField Label emailLabel;
	@UiField Label publicDataLabel;
	@UiField Label personalDataLabel;
	@UiField RaphaelButtonWidget deleteUserProfileButton;
	@UiField RaphaelButtonWidget editUserProfileButton;
	@UiField RaphaelButtonWidget addMessageButton;
	@UiField HTMLPanel descriptionContainer;
	
	@UiField
	HTMLPanel userTextsContainer;
	
	@UiField Label userTextsLabel;
	
	//TODO add description container, use content presenter, display with comments as well
	private Presenter presenter;

	public UserProfileViewImpl() {
		//TODO enable addMessageButton only if user is logged in. give user a choice to enable receiving messages from guests in the future
		initWidget(uiBinder.createAndBindUi(this));
		personalPanel.setVisible(false);
		adminPanel.setVisible(false);
		displayNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.display_name));
		signupDateLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.signup_date));
		firstNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.first_name));
		lastNameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.last_name));
		birthDateLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.birth_date));
		genderLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.gender));
		//emailLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.email_address));
		publicDataLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.public_data));
		personalDataLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.personal_data));
		userTextsLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.author_texts));
		refreshControlsVisibility();
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public Widget asWidget() {
		return this;
	}

	@UiHandler("addMessageButton")
	void onAddMessageButtonClicked(ClickEvent event) {
		logger.log(Level.FINE, "addMessageButton");
		if (presenter != null) {
			presenter.onAddMessageButtonClicked();
		}
	}

	@UiHandler("editUserProfileButton")
	void onEditUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditUserProfileButtonClicked();
		}
	}
	
	@UiHandler("deleteUserProfileButton")
	void onDeleteUserProfileButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteUserProfileButtonClicked();
		}
	}

	@Override
	public void displayUserProfile() {
		//TODO display content related to user (description)
		displayName.setText(presenter.getUserDTO().getUsername());
		//TODO i18n
		signupDate.setText(LocalStorage.getInstance().getDateTimeformat().format(presenter.getUserDTO().getSignupDate()));
		firstName.setText(presenter.getUserDTO().getFirstName());
		lastName.setText(presenter.getUserDTO().getLastName());
		//TODO i18n
		if (presenter.getUserDTO().getBirthDate() != null) {
			birthDate.setText(LocalStorage.getInstance().getDateTimeformat().format(presenter.getUserDTO().getBirthDate()));
		}		
		//TODO switch to toString when implemented
		if (presenter.getUserDTO().getGender() != null) {
			gender.setText(presenter.getUserDTO().getGender().name());
		}		
		//email.setText(presenter.getUserDTO().getEmail());
		
		LoginPresenter loginPresenter = (LoginPresenter)LocalStorage.getInstance().getPresenter(PresenterName.login.name());
		if ((loginPresenter != null) && (loginPresenter.getUserDTO() != null) && (loginPresenter.getUserDTO().getUsername().equals(presenter.getUserDTO().getUsername()))) {
			adminPanel.setVisible(true);
		} else {
			adminPanel.setVisible(false);
		}
		if (presenter.getUserDTO().getShowPersonal() != null && presenter.getUserDTO().getShowPersonal()) {
			personalPanel.setVisible(true);
		} else {
			personalPanel.setVisible(false);
		}
		descriptionContainer.clear();
		presenter.getDescriptionContentPresenter().setContentDTO(presenter.getUserDTO().getDescriptionContent());
		presenter.getDescriptionContentPresenter().go(descriptionContainer);
	}

	@Override
	public void displayUserTexts() {
		ItemListPresenter itemListPresenter = presenter.getUserTextsListPresenter();
		itemListPresenter.setItemListDTO(presenter.getUserTextsItemListDTO(), ItemListType.simplePaging);
		itemListPresenter.go(userTextsContainer);
	}
	
	@Override
	public void refreshControlsVisibility() {
		//TODO
	}

}