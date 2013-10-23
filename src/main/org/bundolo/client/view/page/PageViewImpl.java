package org.bundolo.client.view.page;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.ContentPresenter;
import org.bundolo.client.raphael.Illustration;
import org.bundolo.client.resource.IconResource;
import org.bundolo.client.widget.ConditionalPanel;
import org.bundolo.client.widget.ImageAnchor;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.Constants;
import org.bundolo.shared.model.PageDTO;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class PageViewImpl extends Composite implements PageView {
	
	private static final int sidebarWidth = 300;
	private static final int hoverBarWidth = 20;

	private static final Logger logger = Logger.getLogger(PageViewImpl.class.getName());

	@UiTemplate("PageView.ui.xml")
	interface PageViewUiBinder extends UiBinder<Widget, PageViewImpl> {
	}

	private static PageViewUiBinder uiBinder = GWT.create(PageViewUiBinder.class);

	@UiField ConditionalPanel pageControlsPanel;
	
//	@UiField
//	HorizontalPanel middle;
	
//	@UiField
//	HTMLPanel pageContainer;
	
	@UiField
	HTMLPanel navigation;
	
//	@UiField
//	HTMLPanel pageHeader;
	
//	@UiField
//	HTMLPanel navigationContainer;

	@UiField
	HTMLPanel login;
	
	@UiField
	HTMLPanel illustration;

	@UiField
	HTMLPanel sidebar;

	@UiField
	HTMLPanel hoverBar;

	@UiField
	HTMLPanel animatedArea;

	@UiField
	HTMLPanel pageSpecific;

	@UiField
	HTMLPanel pageContent;

//	@UiField
//	HTMLPanel contentControls;

	@UiField
	RaphaelButtonWidget addPageButton;

	@UiField
	RaphaelButtonWidget deletePageButton;

	@UiField
	RaphaelButtonWidget editPageButton;

//	@UiField(provided = true)
//	PushButton addContentButton;

	@UiField
	CaptionPanel pageDataPanel;
	
//	@UiField
//	HTMLPanel pageData;

	@UiField
	ConditionalPanel mainPanel;

	@UiField
	ConditionalPanel contextPanel;

	@UiField
	HTMLPanel context;
	
	@UiField
	HTMLPanel sidebarContent;
	
	@UiField
	HTMLPanel pageSpecificList;
	
	@UiField
	ImageAnchor footerEmailAddress;	
	@UiField
	ImageAnchor footerFacebookProfile;
	
	private Element sidebarTd;

	private Presenter presenter;

	@UiField(provided = true)
	ToggleButton controlButton;

	enum SidebarStatusType {
		hidden, shown, hiding, showing, pinned
	}

	SidebarStatusType sidebarStatus;

	public PageViewImpl() {
		logger.log(Level.FINE, "constructor");
//		addContentButton = new PushButton(new Image(LocalStorage.getInstance().getIconResource().commentPlus()));
//		addContentButton.setTitle(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.add_comment));
		controlButton = new ToggleButton(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.detach), LocalStorage.getInstance().getMessageResource().getLabel(LabelType.attach));
		
//		contentControls = new HTMLPanel("");
//		contentControls.add(addContentButton);
		initWidget(uiBinder.createAndBindUi(this));
		footerEmailAddress.setHref("malto:" + Constants.BUNDOLO_EMAIL_ADDRESS);
		//footerEmailAddress.setTarget("_blank");
		footerEmailAddress.setResource(LocalStorage.getInstance().getIconResource().messagePlus());
		footerFacebookProfile.setHref(Constants.BUNDOLO_FACEBOOK);
		footerFacebookProfile.setTarget("_blank");
		footerFacebookProfile.setResource(LocalStorage.getInstance().getIconResource().facebook());
		
//		NodeList<Element> tdElements = middle.getElement().getElementsByTagName("td");
//		if (tdElements != null) {
//			if (tdElements.getLength() > 1) {
//				sidebarTd = tdElements.getItem(1);
//			}
//		}
		
		
		sidebarStatus = SidebarStatusType.pinned;
		hoverBar.setWidth("0px");
		//sidebar.setWidth(sidebarWidth + "px");
		animatedArea.setWidth(sidebarWidth + "px");
		resizeAnimation = new ResizeWidthAnimation();

		sidebar.addDomHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				// Window.alert("onMouseOver");
				if (sidebarStatus == SidebarStatusType.hidden) {
					sidebarStatus = SidebarStatusType.showing;
					resizeAnimation.run(100);
				}
			}
		}, MouseOverEvent.getType());

		sidebar.addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// Window.alert("onMouseOut");
				if (sidebarStatus == SidebarStatusType.shown) {
					sidebarStatus = SidebarStatusType.hiding;
					resizeAnimation.run(100);
				}
			}
		}, MouseOutEvent.getType());

		displayMainPanel();

		//TODO display currentUserMessage;
		
//		sidebarContent.add(new SidebarStackPanel(Unit.PX));
		
		//sidebarContent.add(LocalStorage.getInstance().getNotificationPopup());
		
		LocalStorage.getInstance().getPresenter(PresenterName.sidebar.name()).go(sidebarContent);
		
		navigation.add(LocalStorage.getInstance().getNavigation());

		LocalStorage.getInstance().getPageService().getAsciiArt("kiloster", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
//				illustration.add(new Label(caught.getMessage()));
			}

			@Override
			public void onSuccess(String result) {
				logger.log(Level.FINEST, result);
				illustration.add(new Illustration(400, 200, result));
			}});
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		refreshControlsVisibility();
	}

	@Override
	public void displayNavigationData(List<Object> navigationData) {
//		navigation.clear();
//		navigation.add(constructMenu(navigationData));
		LocalStorage.getInstance().getNavigation().drawMenu(navigationData);
		// header.add(constructMenu(presenter.getNavigationData()));
	}

	@UiHandler("addPageButton")
	void onAddButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onAddPageButtonClicked();
		}
	}

	@UiHandler("deletePageButton")
	void onDeleteButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onDeletePageButtonClicked();
		}
	}

	@UiHandler("editPageButton")
	void onEditPageButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onEditPageButtonClicked();
		}
	}

//	 @UiHandler("addContentButton")
//	 void onAddContentButtonClicked(ClickEvent event) {
//	 if (presenter != null) {
//	 presenter.onAddContentButtonClicked();
//	 }
//	 }

	@UiHandler("controlButton")
	void onControlButtonClicked(ClickEvent event) {
		// Window.alert("clicknuo");
		if (sidebarStatus == SidebarStatusType.pinned) {
			sidebarStatus = SidebarStatusType.shown;
			hoverBar.setWidth(hoverBarWidth +"px");
			//sidebar.setWidth(sidebarWidth + "px");
			animatedArea.setWidth(sidebarWidth - hoverBarWidth + "px");
			sidebarTd.setAttribute("width", sidebarWidth + "px");
		} else if (sidebarStatus == SidebarStatusType.shown) {
			sidebarStatus = SidebarStatusType.pinned;
			hoverBar.setWidth("0px");
			//sidebar.setWidth(sidebarWidth + "px");
			animatedArea.setWidth(sidebarWidth + "px");
			sidebarTd.setAttribute("width", sidebarWidth + "px");
		}
	}

	public Widget asWidget() {
		LocalStorage.getInstance().getPresenter(PresenterName.login.name()).go(login);
		
		return this;
	}

	@Override
	public void displayCurrentPageData() {
		//pageData.clear();
		pageContent.clear();
		//pageComments.clear();
		pageDataPanel.setCaptionText(presenter.getCurrentPage().getDescriptionContent().getName());
		//pageData.add(new Label(presenter.getCurrentPage().getDescriptionContent().getName()));
		//pageContent.add(new HTML(presenter.getCurrentPage().getDescriptionContent().getText()));
		ContentPresenter contentPresenter = (ContentPresenter) LocalStorage.getInstance().getPresenter(PresenterName.content.name());
		contentPresenter.setContentDTO(presenter.getCurrentPage().getDescriptionContent());
		contentPresenter.go(pageContent);
	}

	// @Override
	// public void displayPageSpecificWidget() {
	// pageSpecific.clear();
	// pageSpecific.add(presenter.getPageSpecificWidget());
	// }

	ResizeWidthAnimation resizeAnimation;

	public class ResizeWidthAnimation extends Animation {

		@Override
		protected void onComplete() {
			super.onComplete();
			if (sidebarStatus == SidebarStatusType.hiding) {
				sidebarStatus = SidebarStatusType.hidden;
			} else if (sidebarStatus == SidebarStatusType.showing) {
				sidebarStatus = SidebarStatusType.shown;
			}
		}

		@Override
		protected void onUpdate(double progress) {
			if (sidebarStatus == SidebarStatusType.hiding) {
				// direction = -1;
				progress = 1 - progress;
			} else if (sidebarStatus == SidebarStatusType.showing) {
				// direction = 1;

			}
			sidebarTd.setAttribute("width", (int) (progress * (sidebarWidth - hoverBarWidth) + hoverBarWidth) + "px");
			//sidebar.setWidth((int) (progress * sidebarWidth + hoverBarWidth) + "px");
			animatedArea.setWidth((int) (progress * (sidebarWidth - hoverBarWidth)) + "px");
		}
	}

	@Override
	public void displayMainPanel() {
		contextPanel.setVisible(false);
		mainPanel.setVisible(true);
		
		sidebarTd = DOM.getElementById("sidebarCell");
		logger.log(Level.FINEST, "sidebarTd: " + sidebarTd);
		if (sidebarTd != null) {
			sidebarTd.setAttribute("width", sidebarWidth + "px");
		}
		//animatedArea.setHeight(Integer.toString(sidebar.getOffsetHeight()));
	}

	@Override
	public void displayContextPanel(org.bundolo.client.presenter.Presenter presenter) {
		context.clear();
		presenter.go(context);
		mainPanel.setVisible(false);
		contextPanel.setVisible(true);
		//sidebar.setHeight(Integer.toString(contextPanel.getOffsetHeight()));
	}

	//TODO switch to page content name instead of label
	private String getImageTagAsString(PageDTO pageDTO) {
		ImageResource imageResource;
		IconResource iconResource = LocalStorage.getInstance().getIconResource();
		switch (pageDTO.getKind()) {
		case home:
			imageResource = iconResource.home();
			break;
		case forum:
			imageResource = iconResource.forum();
			break;
		case contests:
			imageResource = iconResource.contest();
			break;
		case events:
			imageResource = iconResource.event();
			break;
		case authors:
			imageResource = iconResource.user();
			break;
		case texts:
			imageResource = iconResource.text();
			break;
		case lists:
			imageResource = iconResource.itemList();
			break;
		case connections:
			imageResource = iconResource.link();
			break;
		case news:
			imageResource = iconResource.news();
			break;
		case serials:
			imageResource = iconResource.serial();
			break;
//		case episode:
//			imageResource = iconResource.episode();
//			break;
		case about:
			imageResource = iconResource.about();
			break;
		case custom:
			imageResource = iconResource.custom();
			break;
		default:
			imageResource = iconResource.custom();
			break;
		}
		Image image = new Image(imageResource);
		//TODO i18n
		String label = presenter.getPageDisplayName(pageDTO);
		image.setTitle(label);
		image.setAltText(label);
		return image.getElement().getString();
	}

	@Override
	public void displayPageSpecificPanel(List<Object> navigationPages) {
		logger.log(Level.FINE, "displayPageSpecificPanel: ");
		pageSpecific.clear();
		org.bundolo.client.presenter.Presenter pageSpecificPresenter = presenter.getPageSpecificPresenter(navigationPages);
		if (pageSpecificPresenter != null) {
			logger.log(Level.FINE, "adding page specific: " + pageSpecificPresenter);
			pageSpecificPresenter.go(pageSpecific);
		}
	}

	@Override
	public void displayPageSpecificList(org.bundolo.client.presenter.Presenter presenter) {
		presenter.go(pageSpecificList);
	}

	@Override
	public void refreshControlsVisibility() {
		pageControlsPanel.setVisible(LocalStorage.getInstance().isUserAdmin());
	}

}