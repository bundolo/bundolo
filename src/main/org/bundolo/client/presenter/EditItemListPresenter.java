package org.bundolo.client.presenter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.list.AddItemListEvent;
import org.bundolo.client.event.list.AddItemListEventHandler;
import org.bundolo.client.event.list.EditItemListEvent;
import org.bundolo.client.event.list.EditItemListEventHandler;
import org.bundolo.client.event.list.ItemListUpdatedEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.list.EditItemListView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditItemListPresenter implements Presenter, EditItemListView.Presenter {

	private static final Logger logger = Logger.getLogger(EditItemListPresenter.class.getName());

	private ItemListDTO itemListDTO;
	private final ItemListServiceAsync itemListService;
	private final HandlerManager eventBus;
	private final EditItemListView view;
	private ValidationProcessor validator;

	public EditItemListPresenter(ItemListServiceAsync itemListService, HandlerManager eventBus, EditItemListView view) {
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void onSaveItemListButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelItemListButtonClicked() {
		doEditItemListCancelled();
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
		itemListDTO.setItemListStatus(view.getItemListStatus());
		itemListDTO.setKind(view.getKind());
		//itemListDTO.setName(view.getName());
		itemListDTO.setQuery(view.getQuery());
		if (itemListDTO.getItemListId() == null) {
			itemListDTO.setCreationDate(new Date());
			
			itemListService.saveItemList(itemListDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving ItemList: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "doSave onSuccess");
					//TODO ItemListUpdatedEvent is not processed anywhere at the moment
					eventBus.fireEvent(new ItemListUpdatedEvent(itemListDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_added)));
				}
			});
		} else {
			itemListService.updateItemList(itemListDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating ItemList: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "doSave onSuccess");
					//TODO ItemListUpdatedEvent is not processed anywhere at the moment
					eventBus.fireEvent(new ItemListUpdatedEvent(itemListDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_updated)));
				}
			});
		}
		
	}

	private void doEditItemListCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddItemListEvent.TYPE, new AddItemListEventHandler() {
			public void onAddItemList(AddItemListEvent event) {
				doAddItemList();
			}
		});

		eventBus.addHandler(EditItemListEvent.TYPE, new EditItemListEventHandler() {
			public void onEditItemList(EditItemListEvent event) {
				doEditItemList(event.getItemListDTO());
			}
		});
	}
	
	protected void doAddItemList() {
		ItemListDTO newItemListDTO = new ItemListDTO();
		setItemListDTO(newItemListDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addList);
	}

	private void doEditItemList(ItemListDTO itemListDTO) {
		setItemListDTO(itemListDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editList);
	}

	@Override
	public ItemListDTO getItemListDTO() {
		return itemListDTO;
	}

	@Override
	public void setItemListDTO(ItemListDTO itemListDTO) {
		this.itemListDTO = itemListDTO;
	}
	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}