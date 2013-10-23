package org.bundolo.client.view.list;

import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.widget.ConditionalPanel;
import org.bundolo.client.widget.RangeLabelPager;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.client.widget.ShowMorePagerPanel;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.UserDTO;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ItemListViewImpl extends Composite implements ItemListView {

	private static final Logger logger = Logger.getLogger(ItemListViewImpl.class.getName());

	@UiTemplate("ItemListView.ui.xml")
	interface ItemListViewUiBinder extends UiBinder<Widget, ItemListViewImpl> {}
	private static ItemListViewUiBinder uiBinder = GWT.create(ItemListViewUiBinder.class);

	@UiField ConditionalPanel itemListControlsPanel;
	
	@UiField
	HTMLPanel itemListContainer;

	@UiField
	RaphaelButtonWidget addItemListButton;
	@UiField
	RaphaelButtonWidget deleteItemListButton;
	@UiField
	RaphaelButtonWidget editItemListButton;

	@UiField ShowMorePagerPanel pagerPanel;
	@UiField RangeLabelPager rangeLabelPager;
	@UiField(provided = true)
	SimplePager simplePager = new SimplePager(SimplePager.TextLocation.CENTER, (SimplePager.Resources) GWT.create(SimplePager.Resources.class), true, Constants.DEFAULT_PAGING_PAGE_SIZE * 5, true);
	@UiField(provided = true)
	SimplePager tableDatePager = new SimplePager(SimplePager.TextLocation.CENTER, (SimplePager.Resources) GWT.create(SimplePager.Resources.class), true, Constants.DETAIL_PAGING_PAGE_SIZE * 5, true);;

	@UiField ScrollPanel simplePagerCellListContainer;
	@UiField HTMLPanel noPagerCellListContainer;
	@UiField ScrollPanel tableDateContainer;

	@UiField ConditionalPanel scrollingPagerPanel;
	@UiField ConditionalPanel noPagerPanel;
	@UiField ConditionalPanel simplePagerPanel;
	@UiField ConditionalPanel tableDatePanel;

	private CellTable cellTable;

	private CellList cellList;
	SingleSelectionModel<?> selectionModel = null;

	private Presenter presenter;

	private ItemListType itemListType;

	public ItemListViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public void displayItemList(ItemListType itemListType) {
		this.itemListType = itemListType;
		switch (itemListType) {
		case simpleCellTable:
			initSimpleCellTable();
			break;
		case fullCellTable:
			initFullCellTable();
			break;
		case simpleCellTablePaging:
			initSimplePagingCellTable();
			break;
		default:
			initCellList();
			break;
		}
		refreshControlsVisibility();
	}

	@UiHandler("addItemListButton")
	void onAddItemListButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddItemListButtonClicked();
		}
	}

	@UiHandler("deleteItemListButton")
	void onDeleteItemListButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeleteItemListButtonClicked();
		}
	}

	@UiHandler("editItemListButton")
	void onEditItemListButtonClicked(ClickEvent event) {
		logger.severe("editItemListButton");
		if (presenter != null) {
			presenter.onEditItemListButtonClicked();
		}
	}

	@Override
	public AbstractHasData getDataHolder() {
		if (ItemListType.simpleCellTable.equals(itemListType) || ItemListType.fullCellTable.equals(itemListType) || ItemListType.simpleCellTablePaging.equals(itemListType)) {
			return cellTable;
		} else {
			return cellList;
		}
	}

	private void initCellList() {
		ContentCell contentCell = new ContentCell();
		switch (presenter.getItemList().getKind()) {
		case Content:
			cellList = new CellList<ContentDTO>(contentCell, (CellListResources)GWT.create(CellListResources.class), ContentDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ContentDTO>(ContentDTO.KEY_PROVIDER);
			break;
		case UserProfile:
			cellList = new CellList<UserDTO>(contentCell, UserDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<UserDTO>(UserDTO.KEY_PROVIDER);
			break;
		case Contest:
			cellList = new CellList<ContestDTO>(contentCell, ContestDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ContestDTO>(ContestDTO.KEY_PROVIDER);
			break;
		case Connection:
			cellList = new CellList<ConnectionDTO>(contentCell, ConnectionDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
			break;
		}
		cellList.setPageSize(Constants.DEFAULT_PAGING_PAGE_SIZE);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				presenter.itemSelected(selectionModel.getSelectedObject());
			}
		});
		switch (itemListType) {
		case scrolling:
			pagerPanel.setDisplay(cellList);
			rangeLabelPager.setDisplay(cellList);
			scrollingPagerPanel.setVisible(true);
			noPagerPanel.setVisible(false);
			simplePagerPanel.setVisible(false);
			break;
		case noPaging:
			noPagerCellListContainer.clear();
			noPagerCellListContainer.add(cellList);
			scrollingPagerPanel.setVisible(false);
			noPagerPanel.setVisible(true);
			simplePagerPanel.setVisible(false);
			break;
		case simplePaging:
			simplePager.setDisplay(cellList);
			simplePager.setPageSize(20);
			simplePager.setRangeLimited(true);
			simplePagerCellListContainer.clear();
			simplePagerCellListContainer.add(cellList);
			scrollingPagerPanel.setVisible(false);
			noPagerPanel.setVisible(false);
			simplePagerPanel.setVisible(true);
			break;
		}
		tableDatePanel.setVisible(false);
	}

	private void initSimpleCellTable() {
		Column titleColumn = null;
		TextColumn dateColumn = null;
		switch (presenter.getItemList().getKind()) {
		case Content:
			cellTable = new CellTable<ContentDTO>(Constants.DEFAULT_PAGING_PAGE_SIZE, (CellTableResources)GWT.create(CellTableResources.class), ContentDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ContentDTO>(ContentDTO.KEY_PROVIDER);
			titleColumn = new Column<ContentDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ContentDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getText());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<ContentDTO>() {
				@Override
				public String getValue(ContentDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		case UserProfile:
			cellTable = new CellTable<UserDTO>(UserDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<UserDTO>(UserDTO.KEY_PROVIDER);
			titleColumn = new Column<UserDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(UserDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getUsername());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<UserDTO>() {
				@Override
				public String getValue(UserDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getSignupDate());
				}
			};
			break;
		case Contest:
			cellTable = new CellTable<ContestDTO>(ContestDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ContestDTO>(ContestDTO.KEY_PROVIDER);
			titleColumn = new Column<ContestDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ContestDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getDescriptionContent().getName());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<ContestDTO>() {
				@Override
				public String getValue(ContestDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		case Connection:
			cellTable = new CellTable<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
			titleColumn = new Column<ConnectionDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ConnectionDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getDescriptionContent().getName());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<ConnectionDTO>() {
				@Override
				public String getValue(ConnectionDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		}
		tableDatePager.setDisplay(cellTable);
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				presenter.itemSelected(selectionModel.getSelectedObject());
			}
		});
		
		cellTable.addColumn(titleColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_title));
		cellTable.addColumn(dateColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_date));
		cellTable.setWidth("100%", true);
		cellTable.setColumnWidth(titleColumn, 100, Unit.PCT);
		cellTable.setColumnWidth(dateColumn, 110, Unit.PX);
		
		tableDateContainer.clear();
		tableDateContainer.add(cellTable);
		scrollingPagerPanel.setVisible(false);
		noPagerPanel.setVisible(false);
		simplePagerPanel.setVisible(false);
		tableDatePanel.setVisible(true);
		tableDatePager.setVisible(false);
	}

	private void initSimplePagingCellTable() {
		Column titleColumn = null;
		TextColumn dateColumn = null;
		switch (presenter.getItemList().getKind()) {
		case Content:
			cellTable = new CellTable<ContentDTO>(Constants.DETAIL_PAGING_PAGE_SIZE, (CellTableResources)GWT.create(CellTableResources.class), ContentDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ContentDTO>(ContentDTO.KEY_PROVIDER);
			titleColumn = new Column<ContentDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ContentDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getText());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<ContentDTO>() {
				@Override
				public String getValue(ContentDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		case UserProfile:
			cellTable = new CellTable<UserDTO>(UserDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<UserDTO>(UserDTO.KEY_PROVIDER);
			titleColumn = new Column<UserDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(UserDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getUsername());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<UserDTO>() {
				@Override
				public String getValue(UserDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getSignupDate());
				}
			};
			break;
		case Contest:
			cellTable = new CellTable<ContestDTO>(ContestDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ContestDTO>(ContestDTO.KEY_PROVIDER);
			titleColumn = new Column<ContestDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ContestDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getDescriptionContent().getName());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<ContestDTO>() {
				@Override
				public String getValue(ContestDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		case Connection:
			cellTable = new CellTable<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
			selectionModel = new SingleSelectionModel<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
			titleColumn = new Column<ConnectionDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ConnectionDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getDescriptionContent().getName());
					return sb.toSafeHtml();
				}
			};
			dateColumn = new TextColumn<ConnectionDTO>() {
				@Override
				public String getValue(ConnectionDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		}
		tableDatePager.setDisplay(cellTable);
		cellTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				presenter.itemSelected(selectionModel.getSelectedObject());
			}
		});
		cellTable.addColumn(titleColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_title));
		cellTable.addColumn(dateColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_date));
		cellTable.setWidth("100%", true);
		cellTable.setColumnWidth(titleColumn, 100, Unit.PCT);
		cellTable.setColumnWidth(dateColumn, 110, Unit.PX);
		tableDateContainer.clear();
		tableDateContainer.add(cellTable);
		scrollingPagerPanel.setVisible(false);
		noPagerPanel.setVisible(false);
		simplePagerPanel.setVisible(false);
		tableDatePanel.setVisible(true);
		tableDatePager.setVisible(true);
	}

	private void initFullCellTable() {
		Column titleColumn = null;
		TextColumn dateColumn = null;
		TextColumn authorColumn = null;
		switch (presenter.getItemList().getKind()) {
		case Content:
			cellTable = new CellTable<ContentDTO>(Constants.DETAIL_PAGING_PAGE_SIZE, (CellTableResources)GWT.create(CellTableResources.class), ContentDTO.KEY_PROVIDER);
//			selectionModel = new SingleSelectionModel<ContentDTO>(ContentDTO.KEY_PROVIDER);
			titleColumn = new Column<ContentDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ContentDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getText());
					return sb.toSafeHtml();
				}
			};
			authorColumn = new TextColumn<ContentDTO>() {
				@Override
				public String getValue(ContentDTO object) {
					return object.getAuthorDisplayName();
				}
			};
			dateColumn = new TextColumn<ContentDTO>() {
				@Override
				public String getValue(ContentDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		case UserProfile:
			cellTable = new CellTable<UserDTO>(UserDTO.KEY_PROVIDER);
//			selectionModel = new SingleSelectionModel<UserDTO>(UserDTO.KEY_PROVIDER);
			titleColumn = new Column<UserDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(UserDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getUsername());
					return sb.toSafeHtml();
				}
			};
			authorColumn = new TextColumn<UserDTO>() {
				@Override
				public String getValue(UserDTO object) {
					return object.getDescriptionContent().getText();
				}
			};
			dateColumn = new TextColumn<UserDTO>() {
				@Override
				public String getValue(UserDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getSignupDate());
				}
			};
			break;
		case Contest:
			cellTable = new CellTable<ContestDTO>(ContestDTO.KEY_PROVIDER);
//			selectionModel = new SingleSelectionModel<ContestDTO>(ContestDTO.KEY_PROVIDER);
			titleColumn = new Column<ContestDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ContestDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getDescriptionContent().getName());
					return sb.toSafeHtml();
				}
			};
			authorColumn = new TextColumn<ContestDTO>() {
				@Override
				public String getValue(ContestDTO object) {
					return object.getAuthorDisplayName();
				}
			};
			dateColumn = new TextColumn<ContestDTO>() {
				@Override
				public String getValue(ContestDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		case Connection:
			cellTable = new CellTable<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
//			selectionModel = new SingleSelectionModel<ConnectionDTO>(ConnectionDTO.KEY_PROVIDER);
			titleColumn = new Column<ConnectionDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ConnectionDTO object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant(object.getDescriptionContent().getName());
					return sb.toSafeHtml();
				}
			};
			authorColumn = new TextColumn<ConnectionDTO>() {
				@Override
				public String getValue(ConnectionDTO object) {
					return object.getAuthorDisplayName();
				}
			};
			dateColumn = new TextColumn<ConnectionDTO>() {
				@Override
				public String getValue(ConnectionDTO object) {
					return LocalStorage.getInstance().getDateTimeformat().format(object.getCreationDate());
				}
			};
			break;
		}
		tableDatePager.setDisplay(cellTable);
//		cellTable.setSelectionModel(selectionModel);
//		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			public void onSelectionChange(SelectionChangeEvent event) {
//				presenter.itemSelected(selectionModel.getSelectedObject());
//			}
//		});
		cellTable.addColumn(dateColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_date));
		cellTable.addColumn(authorColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_author));
		cellTable.addColumn(titleColumn, LocalStorage.getInstance().getMessageResource().getLabel(LabelType.table_title));
		cellTable.setWidth("100%", true);
		cellTable.setColumnWidth(titleColumn, 100, Unit.PCT);
		cellTable.setColumnWidth(authorColumn, 200, Unit.PX);
		cellTable.setColumnWidth(dateColumn, 110, Unit.PX);
		cellTable.addStyleName("fullCellTable");
		tableDateContainer.clear();
		tableDateContainer.add(cellTable);
		scrollingPagerPanel.setVisible(false);
		noPagerPanel.setVisible(false);
		simplePagerPanel.setVisible(false);
		tableDatePanel.setVisible(true);
		tableDatePager.setVisible(true);
	}

	@Override
	public void refreshControlsVisibility() {
		//TODO this is going to be changed once item lists become available to users
		itemListControlsPanel.setVisible(LocalStorage.getInstance().isUserAdmin());
	}

}