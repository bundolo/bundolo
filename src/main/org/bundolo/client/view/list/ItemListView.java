package org.bundolo.client.view.list;

import org.bundolo.shared.model.ItemListDTO;

import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;

public interface ItemListView {

	public interface Presenter {
		void onAddItemListButtonClicked();
		void onDeleteItemListButtonClicked();
		void onEditItemListButtonClicked();
		ItemListDTO getItemList();
		void setItemListDTO(ItemListDTO itemListDTO, ItemListType itemListType);
		//List<?> getListItems();
		int getItemListCount();
		
		//AsyncDataProvider<ContentDTO> getProvider();
		void itemSelected(Object selectedItem);
	}
	
	public static enum ItemListType {
		scrolling, noPaging, simplePaging, simpleCellTable, simpleCellTablePaging, fullCellTable
	};

	void setPresenter(Presenter presenter);
	void displayItemList(ItemListType itemListType);
	void refreshControlsVisibility();
	Widget asWidget();
	AbstractHasData getDataHolder();
//	CellList getCellList();
} 