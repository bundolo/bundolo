package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.AddCommentEvent;
import org.bundolo.client.event.comment.AddCommentEventHandler;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.EditCommentEvent;
import org.bundolo.client.event.comment.EditCommentEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.content.EditCommentView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContentServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditCommentPresenter implements Presenter, EditCommentView.Presenter {

	private static final Logger logger = Logger.getLogger(EditCommentPresenter.class.getName());

	private ContentDTO contentDTO;
	private final ContentServiceAsync contentService;
	private final HandlerManager eventBus;
	private final EditCommentView view;
	private ValidationProcessor validator;

	public EditCommentPresenter(ContentServiceAsync contentService, HandlerManager eventBus, EditCommentView view) {
		this.contentService = contentService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		
		//TODO instead of this, we are supposed to set real content
		contentDTO = new ContentDTO();
		bind();
	}

	@Override
	public void onSaveCommentButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelCommentButtonClicked() {
		doEditContentCancelled();
	}

	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "go");
		container.clear();
		container.add(view.asWidget());
	}

	private void doSave() {
		logger.log(Level.FINE, "doSave start");

		//TODO
		//contentDTO.setName(view.getName());
		contentDTO.setText(view.getText());
		//contentDTO.setKind(view.getKind());
		//contentDTO.setContentStatus(view.getContentStatus());
		
		if (contentDTO.getContentId() == null) {
			contentDTO.setContentStatus(ContentStatusType.active); //TODO we don't use other statuses at the moment
			contentService.saveContent(contentDTO, new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "saveContent onSuccess");
					contentDTO.setContentId(result);
					
					//TODO this is a bit of a hack. when user updates comment on his profile, authors should be loaded but it's possible it's not instantiated
					if (ContentKindType.user_comment.equals(contentDTO.getKind())) {
						LocalStorage.getInstance().getPresenter(PresenterName.authors.name());
					}
					eventBus.fireEvent(new CommentUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_added)));
				}
			});
		} else {
			contentService.updateContent(contentDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateContent onSuccess");
					
					//TODO this is a bit of a hack. when user updates comment on his profile, authors should be loaded but it's possible it's not instantiated
					if (ContentKindType.user_comment.equals(contentDTO.getKind())) {
						LocalStorage.getInstance().getPresenter(PresenterName.authors.name());
					}
					eventBus.fireEvent(new CommentUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.comment_updated)));
				}
			});
		}
		
	}

	@Override
	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public void setContentDTO(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	private void doAddComment(ContentDTO contentDTO) {		
		ContentDTO newContentDTO = new ContentDTO();
		newContentDTO.setParentContentId(contentDTO.getContentId());
		newContentDTO.setKind(Utils.getCommentContentKind(contentDTO.getKind()));
		//newContentDTO.setParentPageId(contentDTO.getParentPageId());
		setContentDTO(newContentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addComment);
	}

	private void doEditComment(ContentDTO contentDTO) {		
		setContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editComment);
	}

	private void doEditContentCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddCommentEvent.TYPE, new AddCommentEventHandler() {
			public void onAddComment(AddCommentEvent event) {
				doAddComment(event.getParentContentDTO());
			}
		});

		eventBus.addHandler(EditCommentEvent.TYPE, new EditCommentEventHandler() {
			public void onEditComment(EditCommentEvent event) {
				doEditComment(event.getContentDTO());
			}
		});
	}

	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}
