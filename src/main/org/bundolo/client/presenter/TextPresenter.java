package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.text.AddTextEvent;
import org.bundolo.client.event.text.EditTextEvent;
import org.bundolo.client.event.text.TextUpdatedEvent;
import org.bundolo.client.event.text.TextUpdatedEventHandler;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.content.ContentViewImpl;
import org.bundolo.client.view.content.TextView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContentServiceAsync;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class TextPresenter implements Presenter, TextView.Presenter {

	private static final Logger logger = Logger.getLogger(TextPresenter.class.getName());

	private final HandlerManager eventBus;
	private final TextView view;
	private ItemListDTO textsItemListDTO;
	private ContentDTO textContentDTO;
	private final ItemListServiceAsync itemListService;
	private final ContentServiceAsync contentService;
	private ContentPresenter textContentPresenter;

	public TextPresenter(ContentServiceAsync contentService, ItemListServiceAsync itemListService, HandlerManager eventBus, TextView view) {
		this.contentService = contentService;
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ContentViewImpl contentViewImpl = new ContentViewImpl();
		textContentPresenter = new ContentPresenter(LocalStorage.getInstance().getContentService(), eventBus, contentViewImpl);
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding TextView");
		container.add(view.asWidget());

		itemListService.findItemListByName(ItemListNameType.new_texts.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE),
				new AsyncCallback<ItemListDTO>() {
					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error getting list: ", caught);
						eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
					}

					@Override
					public void onSuccess(ItemListDTO result) {
						textsItemListDTO = result;
						view.displayTexts();
					}
				});
	}
	
	private void doTextUpdated(ContentDTO contentDTO) {
		setTextContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.texts);
	}

	private void bind() {
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.text_comment.equals(event.getUpdatedContent().getKind())) {
					view.displayText();
					Utils.setHistoryTokenIfNeeded(PresenterName.texts);
				}
			}
		});
		
		eventBus.addHandler(TextUpdatedEvent.TYPE, new TextUpdatedEventHandler() {
			public void onTextUpdated(TextUpdatedEvent event) {
				doTextUpdated(event.getUpdatedContent());
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public ItemListDTO getTextsItemListDTO() {
		return textsItemListDTO;
	}

	@Override
	public void setTextContentDTO(ContentDTO contentDTO) {
		logger.log(Level.FINE, "setTextContentDTO: " + contentDTO.getName());
		//TODO get text description first
		this.textContentDTO = contentDTO;
		
		contentService.getDescriptionContent(contentDTO.getContentId(), contentDTO.getKind(), new AsyncCallback<ContentDTO>() {
			@Override
			public void onSuccess(ContentDTO result) {
				logger.log(Level.WARNING, "text description retrieved: " + ((result==null)?"null":result.getText()));
				textContentDTO.setDescriptionContent(result);
				view.displayText();
			}
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error fetching text description", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.description_fetching_error)));
				view.displayText();
			}
		});
	}

	@Override
	public ContentDTO getTextContentDTO() {
		return textContentDTO;
	}

	@Override
	public ContentPresenter getTextContentPresenter() {
		return textContentPresenter;
	}

	@Override
	public void onAddTextButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addText.name());
		eventBus.fireEvent(new AddTextEvent());
	}

	@Override
	public void onDeleteTextButtonClicked() {
		if ((textContentDTO != null) && (textContentDTO.getContentId() != null)) {
			if (Window.confirm(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_delete_confirm))) {
				deleteCurrentText();
			}
		}
	}

	@Override
	public void onEditTextButtonClicked() {
		if ((textContentDTO != null) && (textContentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editText.name());
			eventBus.fireEvent(new EditTextEvent(textContentDTO));
		}
	}
	
	private void deleteCurrentText() {
		contentService.deleteContent(textContentDTO.getContentId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting text: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				//TODO fire some event to refresh texts
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.text_deleted)));
			}}
		);
	}

}
