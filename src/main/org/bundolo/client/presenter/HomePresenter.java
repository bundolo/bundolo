package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.home.HomeView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class HomePresenter implements Presenter, HomeView.Presenter {
	
	private static final Logger logger = Logger.getLogger(HomePresenter.class.getName());

	private final HandlerManager eventBus;
	private final HomeView view;
	//private HasWidgets container;
	private ItemListDTO newTextsItemListDTO;	
	private final ItemListServiceAsync itemListService;

	public HomePresenter(ItemListServiceAsync itemListService, HandlerManager eventBus, HomeView view) {
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding view");
		itemListService.findItemListByName(ItemListNameType.new_texts.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				newTextsItemListDTO = result;
				container.add(view.asWidget());
			}}
		);
	}

	private void bind() {

	}

	@Override
	public ItemListDTO getNewTextsItemListDTO() {
		return newTextsItemListDTO;
	}

}
