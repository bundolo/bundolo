package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.news.AddNewsEvent;
import org.bundolo.client.event.news.AddNewsEventHandler;
import org.bundolo.client.event.news.EditNewsEvent;
import org.bundolo.client.event.news.EditNewsEventHandler;
import org.bundolo.client.event.news.NewsUpdatedEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.news.EditNewsView;
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

public class EditNewsPresenter implements Presenter, EditNewsView.Presenter {

	private static final Logger logger = Logger.getLogger(EditNewsPresenter.class.getName());

	private ContentDTO contentDTO;
	private final ContentServiceAsync contentService;
	private final HandlerManager eventBus;
	private final EditNewsView view;
	private ValidationProcessor validator;

	public EditNewsPresenter(ContentServiceAsync contentService, HandlerManager eventBus, EditNewsView view) {
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
	public void onSaveNewsButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelNewsButtonClicked() {
		doEditNewsCancelled();
	}

	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "go");
		if (!LocalStorage.getInstance().isUserLoggedIn()) {
			History.back();
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.no_permission)));
		}
		container.clear();
		container.add(view.asWidget());
	}

	private void doSave() {
		logger.log(Level.FINE, "doSave start");

		//TODO
		contentDTO.setName(view.getName());
		contentDTO.setText(view.getText());
		//contentDTO.setKind(view.getKind());
		//contentDTO.setContentStatus(view.getContentStatus());
		
		if (contentDTO.getContentId() == null) {
			contentDTO.setContentStatus(ContentStatusType.active); //TODO we don't use other statuses at the moment
			//contentDTO.setKind(ContentKindType.news);
			contentService.saveContent(contentDTO, new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving content: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "saveContent onSuccess");
					contentDTO.setContentId(result);
					eventBus.fireEvent(new NewsUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_added)));
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
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateContent onSuccess");
					eventBus.fireEvent(new NewsUpdatedEvent(contentDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.news_updated)));
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

	private void doAddNews() {		
		ContentDTO newContentDTO = new ContentDTO();
		newContentDTO.setKind(ContentKindType.news);
		//newContentDTO.setParentPageId(contentDTO.getParentPageId());
		setContentDTO(newContentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addNews);
	}

	private void doEditNews(ContentDTO contentDTO) {
		setContentDTO(contentDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editNews);
	}

	private void doEditNewsCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddNewsEvent.TYPE, new AddNewsEventHandler() {
			public void onAddNews(AddNewsEvent event) {
				doAddNews();
			}
		});

		eventBus.addHandler(EditNewsEvent.TYPE, new EditNewsEventHandler() {
			public void onEditNews(EditNewsEvent event) {
				doEditNews(event.getContentDTO());
			}
		});
	}
	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}