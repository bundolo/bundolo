package org.bundolo.client.presenter;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.page.AddPageEvent;
import org.bundolo.client.event.page.AddPageEventHandler;
import org.bundolo.client.event.page.EditPageEvent;
import org.bundolo.client.event.page.EditPageEventHandler;
import org.bundolo.client.event.page.PageUpdatedEvent;
import org.bundolo.client.view.page.EditPageView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.PageDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageStatusType;
import org.bundolo.shared.services.PageServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditPagePresenter implements Presenter, EditPageView.Presenter {

	private static final Logger logger = Logger.getLogger(EditPagePresenter.class.getName());

	private PageDTO currentPage;
	private final PageServiceAsync pageService;
	private final HandlerManager eventBus;
	private final EditPageView view;
	private List<PageDTO> pageChildren;
	private ValidationProcessor validator;

	public EditPagePresenter(PageServiceAsync pageService, HandlerManager eventBus, EditPageView view) {
		this.pageService = pageService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		bind();

	}

	@Override
	public void onSavePageButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelPageButtonClicked() {
		doEditPageCancelled();
	}

	public void go(final HasWidgets container) {
		if (!LocalStorage.getInstance().isUserLoggedIn()) {
			History.back();
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.no_permission)));
		}
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
	private void doSave() {
		logger.log(Level.FINE, "doSave start");
		//currentPage.setName(view.getName());
		currentPage.setKind(view.getKind());

		currentPage.getDescriptionContent().setName(view.getName());
		currentPage.getDescriptionContent().setText(view.getText());
		if (currentPage.getPageId() == null) {
			//this is adding
			logger.log(Level.FINE, "doSave adding");
			currentPage.setPageStatus(PageStatusType.active); //TODO hardcoded for now
			currentPage.getDescriptionContent().setContentStatus(ContentStatusType.active); //TODO hardcoded for now
			
			currentPage.getDescriptionContent().setKind(ContentKindType.page_description);
			currentPage.setAuthorUsername(LocalStorage.getInstance().getUsername());
			currentPage.getDescriptionContent().setAuthorUsername(LocalStorage.getInstance().getUsername());
			currentPage.setCreationDate(new Date());
			currentPage.getDescriptionContent().setCreationDate(new Date());
			pageService.findPagesCount(currentPage.getParentPageId(), new AsyncCallback<Integer>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error getting page children count: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Integer result) {
					logger.log(Level.FINE, "getting page children count onSuccess: " + result);
					currentPage.setDisplayOrder(result);
					savePage(currentPage);
				}
			});
		} else {			
			//this is updating
			updatePage(currentPage);
		}
	}
	
	private void savePage(final PageDTO pageDTO) {
		logger.log(Level.FINE, "savePage display order: " + pageDTO.getDisplayOrder());
		pageService.savePage(pageDTO, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException) {
	                ValidationException ex = (ValidationException) caught;
	                logger.log(Level.FINE, "Server side validation failed: ", ex);
	                validator.processServerErrors(ex);
	            } else {
	            	logger.log(Level.SEVERE, "Error saving page: ", caught);
	            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_adding_failed)));
	            }
			}
			@Override
			public void onSuccess(Void result) {
				logger.log(Level.FINE, "savePage onSuccess");
				//in case of adding, there are no children so we are done
				eventBus.fireEvent(new PageUpdatedEvent(pageDTO));
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_added)));
			}
		});
	}
	
	private void updatePage(PageDTO pageDTO) {
		logger.log(Level.FINE, "updatePage display order: " + pageDTO.getDisplayOrder());
		pageService.updatePage(pageDTO, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException) {
	                ValidationException ex = (ValidationException) caught;
	                logger.log(Level.FINE, "Server side validation failed: ", ex);
	                validator.processServerErrors(ex);
	            } else {
	            	logger.log(Level.SEVERE, "Error updating page: ", caught);
	            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_update_failed)));
	            }
			}
			@Override
			public void onSuccess(Void result) {
				logger.log(Level.FINE, "updatePage onSuccess");
				savePageChildren();
			}
		});
	}
	
	private void savePageChildren() {
		boolean shouldUpdateChildren = false;
		if ((pageChildren != null) && (pageChildren.size() > 0)) {
			logger.log(Level.FINE, "doSave has children");
			List<String> pageChildrenReordered = view.getChildren();
			if (pageChildrenReordered != null) {
				for(final PageDTO pageDTO : pageChildren) {
					if (pageDTO.getDisplayOrder() != pageChildrenReordered.indexOf(pageDTO.getDescriptionContent().getName())) {
						pageDTO.setDisplayOrder(pageChildrenReordered.indexOf(pageDTO.getDescriptionContent().getName()));
						shouldUpdateChildren = true;
					}
				}
				
			}
		}
		if (shouldUpdateChildren) {
			pageService.updatePages(pageChildren, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating pageChildren: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "pageChildren updatePages onSuccess: " + pageChildren.size());
					eventBus.fireEvent(new PageUpdatedEvent(currentPage));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_updated)));
				}
			});
		} else {
			logger.log(Level.FINE, "pageChildren didn't have to be updated");
			eventBus.fireEvent(new PageUpdatedEvent(currentPage));
			eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.page_updated)));
		}
	}

	@Override
	public PageDTO getCurrentPage() {
		return currentPage;
	}

	@Override
	public List<PageDTO> getPageChildren() {
		return pageChildren;
	}

	@Override
	public void setCurrentPage(PageDTO currentPage) {
		
		this.currentPage = currentPage;
		if ((currentPage != null) && (currentPage.getPageId() != null)) {
			logger.log(Level.FINE, "setCurrentPage: " + currentPage.getDescriptionContent().getName());
			pageService.findChildPages(currentPage.getPageId(), new AsyncCallback<List<PageDTO>>() {
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, "Error getting page children: ", caught);
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
				}
				@Override
				public void onSuccess(List<PageDTO> result) {
					logger.log(Level.INFO, "getting page children onSuccess: " + result.size());
					pageChildren = result;
					view.displayChildren();
				}
			});
		} else {
			pageChildren = null;
			view.displayChildren();
		}
	}
	
	private void doAddPage(PageDTO pageDTO) {
		logger.log(Level.FINE, "doAddPage");
		PageDTO newPageDTO = new PageDTO();
		newPageDTO.setParentPageId(pageDTO.getPageId());
		ContentDTO descriptionContent = new ContentDTO();
		newPageDTO.setDescriptionContent(descriptionContent);
		//newPageDTO.setKind(PageKindType.forum);
		//newPageDTO.setPageStatus(PageStatusType.active);
		setCurrentPage(newPageDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addPage);
	}
	
	private void doEditPage(PageDTO pageDTO) {
		logger.log(Level.FINE, "doEditPage: " + pageDTO.getDescriptionContent().getName());
		setCurrentPage(pageDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editPage);
	}
	
	private void doEditPageCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddPageEvent.TYPE, new AddPageEventHandler() {
			public void onAddPage(AddPageEvent event) {
				doAddPage(event.getParentPageDTO());
			}
		});  

		eventBus.addHandler(EditPageEvent.TYPE, new EditPageEventHandler() {
			public void onEditPage(EditPageEvent event) {
				doEditPage(event.getPageDTO());
			}
		});  
	}
}