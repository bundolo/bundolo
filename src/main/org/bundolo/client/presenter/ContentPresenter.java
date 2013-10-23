package org.bundolo.client.presenter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.AddCommentEvent;
import org.bundolo.client.event.comment.EditCommentEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.content.ContentView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.RatingDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContentServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class ContentPresenter implements Presenter, ContentView.Presenter {
	
	private static final Logger logger = Logger.getLogger(ContentPresenter.class.getName());

	private ContentDTO contentDTO;
	//private ContentDTO rootCommentDTO;
	//private PageDTO pageDTO;
	private final ContentServiceAsync contentService;
	private final HandlerManager eventBus;
	private final ContentView view;
	//private HasWidgets container;

	public ContentPresenter(ContentServiceAsync contentService,
			HandlerManager eventBus, ContentView view) {
		this.contentService = contentService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void go(final HasWidgets container) {
		//this.container = container;
		//container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setContentDTO(final ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
		if (contentDTO != null) {
			updateRatingContent(contentDTO);
			contentService.findContents(contentDTO.getContentId(), Utils.getCommentContentKind(contentDTO.getKind()), new AsyncCallback<List<ContentDTO>>() {
				@Override
				public void onSuccess(List<ContentDTO> result) {
					contentDTO.setContentChildren(result);
					view.displayContent();
				}
				@Override
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, "Error fetching comments", caught);
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comments_fetching_error)));
				}
			});
		}
	}

	@Override
	public void onAddCommentButtonClicked(ContentDTO contentDTO) {
		LocalStorage.getInstance().getPresenter(PresenterName.addComment.name());
		eventBus.fireEvent(new AddCommentEvent(contentDTO));
	}

	@Override
	public void onDeleteCommentButtonClicked(ContentDTO contentDTO) {
		//TODO show confirmation message
		if ((contentDTO != null) && (contentDTO.getContentId() != null)) {
			deleteCurrentContent(contentDTO);
		}
	}
	
	@Override
	public void onEditCommentButtonClicked(ContentDTO contentDTO) {
		if ((contentDTO != null) && (contentDTO.getContentId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editComment.name());
			eventBus.fireEvent(new EditCommentEvent(contentDTO));
		}		
	}

	@Override
	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	private void deleteCurrentContent(ContentDTO contentDTO) {
		contentService.deleteContent(contentDTO.getContentId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting content", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_deleting_failed)));
			}

			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_deleted)));
			}}
		);
	}

	private void bind() {
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}
	
	//TODO for now, we keep list of rated content ids on the client side, that is not safe.
	//to improve performance, rating updates should be on the server side, without the need to explicitly invoke from the client.
	//it should be wrapped in retrieval methods in the service somehow
	private void updateRatingContent(final ContentDTO contentDTO) {
		if (contentDTO != null && contentDTO.getContentId() != null) {
			//update only content that is not already rated and that is not written by this user
			//TODO last || is for admins and moderators, their rating should not be updated. that is just a quick fix until rating is fully implemented
			if (!((LocalStorage.getInstance().getRatedContentIds().contains(contentDTO.getContentId())) || (contentDTO.getAuthorUsername() != null && contentDTO.getAuthorUsername().equals(LocalStorage.getInstance().getUsername())) || (ContentKindType.user_description.equals(contentDTO.getKind()) && contentDTO.getRating().getValue() < 0))) {
				if (contentDTO.getRating() == null) {
					RatingDTO ratingDTO = new RatingDTO();
					ratingDTO.setValue(0L);
					contentDTO.setRating(ratingDTO);
				}
				Long ratingValue = contentDTO.getRating().getValue();
				contentDTO.getRating().setValue(ratingValue + 1); //TODO this is going to be modified in the future
				contentService.updateContent(contentDTO, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, "Error updating content: ", caught);
					}
					@Override
					public void onSuccess(Void result) {
						logger.log(Level.FINE, "updateContent onSuccess");
						LocalStorage.getInstance().getRatedContentIds().add(contentDTO.getContentId());
					}
				});
			}
		}
	}

}
