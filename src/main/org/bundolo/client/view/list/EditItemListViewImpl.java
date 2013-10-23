package org.bundolo.client.view.list;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.validation.LabelTextActionExtended;
import org.bundolo.client.validation.NotEmptyValidatorExtended;
import org.bundolo.client.widget.EnumListBox;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.ItemListKindType;
import org.bundolo.shared.model.enumeration.ItemListStatusType;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.actions.LabelTextAction;
import eu.maydu.gwt.validation.client.actions.StyleAction;

public class EditItemListViewImpl extends Composite implements EditItemListView {
	
	private static final Logger logger = Logger.getLogger(EditItemListViewImpl.class.getName());

	@UiTemplate("EditItemListView.ui.xml")
	interface EditItemListViewUiBinder extends UiBinder<Widget, EditItemListViewImpl> {}
	private static EditItemListViewUiBinder uiBinder = GWT.create(EditItemListViewUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField
	TextArea query;

	@UiField(provided = true)
	EnumListBox<ItemListKindType> kind = new EnumListBox<ItemListKindType>(ItemListKindType.class);

	@UiField(provided = true)
	EnumListBox<ItemListStatusType> itemListStatus = new EnumListBox<ItemListStatusType>(ItemListStatusType.class);

	@UiField
	RaphaelButtonWidget saveItemListButton;

	@UiField
	RaphaelButtonWidget cancelItemListButton;
	
	@UiField Label nameLabel;
	@UiField Label queryLabel;
	@UiField Label kindLabel;
	@UiField Label statusLabel;

	private Presenter presenter;

	public EditItemListViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		nameLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_name));
		queryLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_query));
		kindLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_kind));
		statusLabel.setText(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.item_list_status));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		presenter.getValidator().addValidators(LabelType.item_list_name.name(), new NotEmptyValidatorExtended(name).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(nameLabel, LabelType.item_list_name)));
		presenter.getValidator().addValidators(LabelType.item_list_query.name(), new NotEmptyValidatorExtended(query).addActionForFailure(new StyleAction("validationFailedBorder")).addActionForFailure(new LabelTextActionExtended(queryLabel, LabelType.item_list_query)));
		//TODO more validation
	}

	@UiHandler("saveItemListButton")
	void onSaveItemListButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onSaveItemListButtonClicked();
		}
	}

	@UiHandler("cancelItemListButton")
	void onCancelItemListButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onCancelItemListButtonClicked();
		}
	}

	public Widget asWidget() {
		if (presenter != null) {
			//name.setValue(presenter.getItemListDTO().getName());
			query.setValue(presenter.getItemListDTO().getQuery());
			kind.setValue(presenter.getItemListDTO().getKind());
			itemListStatus.setValue(presenter.getItemListDTO().getItemListStatus());
		}
		return this;
	}

	@Override
	public ItemListKindType getKind() {
		return kind.getValue();
	}

	@Override
	public String getName() {
		return name.getValue();
	}

	@Override
	public String getQuery() {
		return query.getValue();
	}

	@Override
	public ItemListStatusType getItemListStatus() {
		return itemListStatus.getValue();
	}

} 