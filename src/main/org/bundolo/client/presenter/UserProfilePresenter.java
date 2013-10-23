package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.list.ItemListRefreshedEvent;
import org.bundolo.client.event.list.ItemListRefreshedEventHandler;
import org.bundolo.client.event.user.AddMessageEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.event.user.UserProfileUpdatedEvent;
import org.bundolo.client.event.user.UserProfileUpdatedEventHandler;
import org.bundolo.client.view.content.ContentViewImpl;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.client.view.user.UserProfileView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.services.UserProfileServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class UserProfilePresenter implements Presenter, UserProfileView.Presenter {

	private static final Logger logger = Logger.getLogger(UserProfilePresenter.class.getName());

	private UserDTO userDTO;
	private final HandlerManager eventBus;
	private final UserProfileView view;
	//private HasWidgets container;
	private ContentPresenter descriptionContentPresenter;
	private ItemListPresenter userTextsListPresenter;
	private ItemListDTO userTextsItemListDTO;

	public UserProfilePresenter(UserProfileServiceAsync userProfileService,
			HandlerManager eventBus, UserProfileView view) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ContentViewImpl contentViewImpl = new ContentViewImpl();
		descriptionContentPresenter = new ContentPresenter(LocalStorage.getInstance().getContentService(), eventBus, contentViewImpl);
		ItemListView userTextsItemListViewImpl = new ItemListViewImpl();
		userTextsListPresenter = new ItemListPresenter(LocalStorage.getInstance().getItemListService(), LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, userTextsItemListViewImpl);		
	}

	@Override
	public void go(final HasWidgets container) {
		//this.container = container;
		//container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void onDeleteUserProfileButtonClicked() {
		//TODO
		//Window.alert("delete");
	}

	@Override
	public void onEditUserProfileButtonClicked() {
		Utils.setHistoryTokenIfNeeded(PresenterName.editUser);
	}

	private void doUserProfileDeleted() {
		//TODO not used since users could not be deleted yet
		//TODO load the same page, not home
		Utils.setHistoryTokenIfNeeded(PresenterName.home);
	}

	private void doUserProfileUpdated(UserDTO userDTO) {
		setUserDTO(userDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.user);
	}

	private void bind() {
		//TODO for now, even if user commented on his user profile page, after comment is saved author page will be displayed
		//		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
		//			public void onCommentUpdated(CommentUpdatedEvent event) {
		//				if (ContentKindType.user_comment.equals(event.getUpdatedContent().getKind())) {
		//					PagePresenter pagePresenter = (PagePresenter) LocalStorage.getInstance().getPresenter(PresenterName.page.name());
		//					if (PageKindType.user.equals(pagePresenter.getCurrentPage().getKind())) {
		//						view.displayUserProfile();
		//						Utils.setHistoryTokenIfNeeded(PresenterName.user);
		//					}
		//				}
		//			}
		//		});

		eventBus.addHandler(UserProfileUpdatedEvent.TYPE, new UserProfileUpdatedEventHandler() {
			public void onUserProfileUpdated(UserProfileUpdatedEvent event) {
				doUserProfileUpdated(event.getUpdatedUserProfile());
			}
		});
		eventBus.addHandler(ItemListRefreshedEvent.TYPE, new ItemListRefreshedEventHandler() {
			@Override
			public void onItemListRefreshed(ItemListRefreshedEvent event) {
				if (ItemListNameType.author_texts.equals(event.getRefreshedItemListName())) {
					storeListAndDisplay(event.getRefreshedItemListName());
				}
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public UserDTO getUserDTO() {
		return userDTO;
	}

	@Override
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
		view.displayUserProfile();
		storeListAndDisplay(ItemListNameType.author_texts);
	}

	@Override
	public void onAddMessageButtonClicked() {
		logger.log(Level.FINE, "onAddMessageButtonClicked");
		LocalStorage.getInstance().getPresenter(PresenterName.addMessage.name());
		eventBus.fireEvent(new AddMessageEvent(userDTO));
	}

	@Override
	public ContentPresenter getDescriptionContentPresenter() {
		return descriptionContentPresenter;
	}

	private void storeListAndDisplay(ItemListNameType itemListName) {
		if (userDTO != null) {
			switch (itemListName) {
			case author_texts:
				userTextsItemListDTO = LocalStorage.getInstance().getItemListDTO(ItemListNameType.author_texts, userDTO.getUsername(), Constants.DEFAULT_LOCALE);
				if (userTextsItemListDTO != null) {
					view.displayUserTexts();
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public ItemListPresenter getUserTextsListPresenter() {
		return userTextsListPresenter;
	}

	@Override
	public ItemListDTO getUserTextsItemListDTO() {
		return userTextsItemListDTO;
	}
}