package org.bundolo.client.widget;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.presenter.ItemListPresenter;
import org.bundolo.client.view.list.ItemListView;
import org.bundolo.client.view.list.ItemListViewImpl;
import org.bundolo.client.view.list.ItemListView.ItemListType;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageKindType;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SidebarStackPanel extends StackLayoutPanel {

	private static final Logger logger = Logger.getLogger(SidebarStackPanel.class.getName());
	
	private final HandlerManager eventBus;

	public SidebarStackPanel(HandlerManager eventBus, Unit unit, ItemListDTO... itemListDTOs) {
		super(unit);
		this.eventBus = eventBus;
		//setPixelSize(300, 700);
		setHeight("100%");
		setWidth("100%");
//		if (itemListDTOs != null && itemListDTOs.length > 0) {
//			for (ItemListDTO itemListDTO: itemListDTOs) {
//				
//			}
//		}
//		
//		ItemListServiceAsync itemListService = LocalStorage.getInstance().getItemListService();
//		ItemListView newTextsItemListView = new ItemListViewImpl();
//		ItemListPresenter newTextsItemListPresenter = new ItemListPresenter(itemListService, LocalStorage.getInstance().getContentService(), LocalStorage.getInstance().getUserProfileService(), LocalStorage.getInstance().getContestService(), LocalStorage.getInstance().getConnectionService(), eventBus, newTextsItemListView);
//		
//		itemListService.findItemListByName(ItemListNameType.new_texts.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				logger.log(Level.SEVERE, "Error getting list: ", caught);
//				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
//			}
//			@Override
//			public void onSuccess(ItemListDTO result) {
//				newTextsItemListDTO = result;
//				container.add(view.asWidget());
//			}}
//		);
//		itemListPresenter.setItemListDTO(itemListDTO, ItemListType.simplePaging);
//		add(itemListView.asWidget(), itemListDTO.getItemListId().toString(), 30);
//		
//		
		// TODO
		VerticalPanel textsPanel = new VerticalPanel();
		for (int i = 0; i < 10; i++) {
			final Anchor textLink = new Anchor("text" + i);
			textsPanel.add(textLink);
		}
		VerticalPanel postsPanel = new VerticalPanel();
		for (int i = 0; i < 10; i++) {
			final Anchor postLink = new Anchor("post" + i);
			postsPanel.add(postLink);
		}
		VerticalPanel newsPanel = new VerticalPanel();
		for (int i = 0; i < 10; i++) {
			final Anchor newsLink = new Anchor("news" + i);
			newsPanel.add(newsLink);
		}
		add(textsPanel, "Texts", 30);
		add(postsPanel, "Posts", 30);
		add(newsPanel, "News", 30);
	}

}
