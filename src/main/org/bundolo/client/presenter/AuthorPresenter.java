package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.MessageSentEvent;
import org.bundolo.client.event.user.MessageSentEventHandler;
import org.bundolo.client.view.user.AuthorView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class AuthorPresenter implements Presenter, AuthorView.Presenter {
	
	private static final Logger logger = Logger.getLogger(AuthorPresenter.class.getName());

	private final HandlerManager eventBus;
	private final AuthorView view;
	private ItemListDTO authorsItemListDTO;
	private UserDTO authorUserDTO;
	private final ItemListServiceAsync itemListService;

	public AuthorPresenter(ItemListServiceAsync itemListService, HandlerManager eventBus, AuthorView view) {
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding view");
		container.add(view.asWidget());
		
		itemListService.findItemListByName(ItemListNameType.authors.getItemListName(), null, null, new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				authorsItemListDTO = result;
				view.displayAuthors();
			}}
		);
	}

	private void bind() {
		eventBus.addHandler(MessageSentEvent.TYPE, new MessageSentEventHandler() {
			public void onMessageSent(MessageSentEvent event) {
				doMessageSent(event.getRecipientUserDTO());
			}
		});
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.user_comment.equals(event.getUpdatedContent().getKind())) {
					if (authorUserDTO == null) {
						LoginPresenter loginPresenter = (LoginPresenter)LocalStorage.getInstance().getPresenter(PresenterName.login.name());
						if ((loginPresenter != null) && (loginPresenter.getUserDTO() != null)) {
							authorUserDTO = loginPresenter.getUserDTO();
						}
					}
					view.displayAuthor();
					Utils.setHistoryTokenIfNeeded(PresenterName.authors);
				}
			}
		});
	}
	
	private void doMessageSent(UserDTO recipientUserDTO) {
		setAuthorUserDTO(recipientUserDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.authors);
	}

	@Override
	public ItemListDTO getAuthorsItemListDTO() {
		return authorsItemListDTO;
	}

	@Override
	public void setAuthorUserDTO(UserDTO userDTO) {
		this.authorUserDTO = userDTO;
		view.displayAuthor();
	}

	@Override
	public UserDTO getAuthorUserDTO() {
		return authorUserDTO;
	}

}
