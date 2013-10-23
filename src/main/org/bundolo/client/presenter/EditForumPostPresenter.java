package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.forum.AddForumPostEvent;
import org.bundolo.client.event.forum.AddForumPostEventHandler;
import org.bundolo.client.event.forum.EditForumPostEvent;
import org.bundolo.client.event.forum.EditForumPostEventHandler;
import org.bundolo.client.event.forum.ForumPostUpdatedEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.forum.EditForumPostView;
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

public class EditForumPostPresenter implements Presenter, EditForumPostView.Presenter {

	private static final Logger logger = Logger.getLogger(EditForumPostPresenter.class.getName());

	private ContentDTO contentDTO;
	private final ContentServiceAsync contentService;
	private final HandlerManager eventBus;
	private final EditForumPostView view;
	private ValidationProcessor validator;

	public EditForumPostPresenter(ContentServiceAsync contentService, HandlerManager eventBus, EditForumPostView view) {
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
	public void onSaveForumPostButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelForumPostButtonClicked() {
		doEditForumCancelled();
	}

	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "go");
		container.clear();
		container.add(view.asWidget());
	}

	private void doSave() {
		logger.log(Level.FINE, "doSave start");

		//TODO
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
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "saveContent onSuccess");
					contentDTO.setContentId(result);
					eventBus.fireEvent(new ForumPostUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_added)));
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
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateContent onSuccess");
					eventBus.fireEvent(new ForumPostUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.forum_post_updated)));
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

	private void doAddForumPost(ContentDTO contentDTO) {
		if (contentDTO != null) {
			ContentDTO newContentDTO = new ContentDTO();
			if (ContentKindType.forum_group.equals(contentDTO.getKind())) {
				newContentDTO.setKind(ContentKindType.forum_topic);
			} else {
				newContentDTO.setKind(ContentKindType.forum_post);
			}			
			newContentDTO.setParentContentId(contentDTO.getContentId());
			setContentDTO(newContentDTO);
			Utils.setHistoryTokenIfNeeded(PresenterName.addForumPost);
		}
	}

	private void doEditForumPost(ContentDTO contentDTO) {
		setContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editForumPost);
	}

	private void doEditForumCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddForumPostEvent.TYPE, new AddForumPostEventHandler() {
			public void onAddForumPost(AddForumPostEvent event) {
				doAddForumPost(event.getParentContentDTO());
			}
		});

		eventBus.addHandler(EditForumPostEvent.TYPE, new EditForumPostEventHandler() {
			public void onEditForumPost(EditForumPostEvent event) {
				doEditForumPost(event.getContentDTO());
			}
		});
	}
	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}