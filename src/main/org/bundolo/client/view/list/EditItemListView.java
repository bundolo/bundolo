package org.bundolo.client.view.list;

import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ItemListKindType;
import org.bundolo.shared.model.enumeration.ItemListStatusType;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditItemListView {

	public interface Presenter {
		void onSaveItemListButtonClicked();
		void onCancelItemListButtonClicked();
		ItemListDTO getItemListDTO();
		void setItemListDTO(ItemListDTO itemListDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getName();
	String getQuery();
	ItemListKindType getKind();
	ItemListStatusType getItemListStatus();
}