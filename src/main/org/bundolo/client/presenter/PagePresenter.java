package org.bundolo.client.presenter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.page.AddPageEvent;
import org.bundolo.client.event.page.EditPageEvent;
import org.bundolo.client.event.page.NavigationPagesLoadedEvent;
import org.bundolo.client.event.page.NavigationPagesLoadedEventHandler;
import org.bundolo.client.event.page.PageDeletedEvent;
import org.bundolo.client.event.page.PageDeletedEventHandler;
import org.bundolo.client.event.page.PageUpdatedEvent;
import org.bundolo.client.event.page.PageUpdatedEventHandler;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.page.PageView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.PageDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.model.enumeration.PageKindType;
import org.bundolo.shared.services.ContentServiceAsync;
import org.bundolo.shared.services.PageServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class PagePresenter implements Presenter, PageView.Presenter {

    private static final Logger logger = Logger.getLogger(PagePresenter.class.getName());

    private PageDTO currentPage;
    private final PageServiceAsync pageService;
    private final ContentServiceAsync contentService;
    private final HandlerManager eventBus;
    private final PageView view;
    Presenter pageSpecificPresenter;

    // private List<Object> navigationPages;

    public PagePresenter(PageServiceAsync pageService, ContentServiceAsync contentService, HandlerManager eventBus,
	    PageView view) {
	this.pageService = pageService;
	this.contentService = contentService;
	this.eventBus = eventBus;
	this.view = view;
	// this.selectionModel = new SelectionModel<PageDTO>();
	this.view.setPresenter(this);
	// this.view.setColumnDefinitions(columnDefinitions);
	bind();
    }

    @Override
    public void onAddPageButtonClicked() {
	LocalStorage.getInstance().getPresenter(PresenterName.addPage.name());
	eventBus.fireEvent(new AddPageEvent(currentPage));
    }

    @Override
    public void onDeletePageButtonClicked() {
	deleteCurrentPage();
    }

    @Override
    public void onEditPageButtonClicked() {
	EditPagePresenter editPagePresenter = (EditPagePresenter) LocalStorage.getInstance().getPresenter(
		PresenterName.editPage.name());
	// editPagePresenter.setCurrentPage(currentPage);

	logger.log(Level.FINE, "EditPageEvent: " + currentPage.getDescriptionContent().getName());
	eventBus.fireEvent(new EditPageEvent(currentPage));
    }

    @Override
    public void onNavigatePageButtonClicked(PageDTO pageDTO) {
	doNavigatePage(pageDTO);
    }

    @Override
    public void go(final HasWidgets container) {
	container.clear();
	container.add(view.asWidget());
	displayMainPanel();
	// TODO navigation should be refreshed sometimes. to be defined.
	// probably some notification driven refresh when pages are modified
	displayPageSpecific();
    }

    // TODO instead of manually finding current page, we might use
    // MenuBar.getSelectedItem() probably
    private void findCurrentPage(List<Object> navigationData) {
	if (navigationData != null) {
	    logger.log(Level.FINE, "searching: " + History.getToken());
	    String token = History.getToken();
	    if (!Utils.hasText(token)) {
		// TODO put constants in some kind of config file
		token = PresenterName.home.name();
	    }
	    currentPage = findPageByDisplayName(navigationData, token);
	    logger.log(Level.FINE, "currentPage just found: " + token);
	    if ((currentPage != null) && (currentPage.getDescriptionContent() != null)) {
		view.displayCurrentPageData();
	    }
	}
    }

    @Override
    public PageDTO getCurrentPage() {
	return currentPage;
    }

    private void deleteCurrentPage() {
	// TODO add confirmation popup
	pageService.deletePage(currentPage.getPageId(), new AsyncCallback<Void>() {

	    @Override
	    public void onFailure(Throwable caught) {
		logger.log(Level.SEVERE, "Error deleting page", caught);
		eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			.getLabel(LabelType.page_deleting_failed)));
	    }

	    @Override
	    public void onSuccess(Void result) {
		LocalStorage.getInstance().refreshNavigationPages();
		eventBus.fireEvent(new PageDeletedEvent());
		eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			.getLabel(LabelType.page_deleted)));
	    }
	});
    }

    private void doPageDeleted() {
	// TODO go to parent page perhaps
	Utils.setHistoryTokenIfNeeded(PresenterName.home);
    }

    private void doPageUpdated() {
	// TODO decide where to go: page just saved might be unreachable
	// normally. in case of adding it makes sense to go to previous page. in
	// case of update it's unclear
	Utils.setHistoryTokenIfNeeded(PresenterName.home);
	LocalStorage.getInstance().refreshNavigationPages();
    }

    private void doNavigatePage(PageDTO pageDTO) {
	Utils.setHistoryTokenIfNeeded(getPageDisplayName(pageDTO));
    }

    private void doNavigationPagesLoaded(List<Object> navigationPages) {
	logger.log(Level.FINE, "doNavigationPagesLoaded: ");

	displayPageSpecific();

	view.displayNavigationData(navigationPages);
	// view.displayPageSpecificPanel();
	if ((currentPage == null) || (!getPageDisplayName(currentPage).equals(History.getToken()))) {
	    logger.log(Level.FINE, "findCurrentPage: " + currentPage + ", " + History.getToken());
	    findCurrentPage(navigationPages);
	    // logger.severe("searching: " + History.getToken());
	    // for (PageDTO tempPage : navigationData) {
	    // logger.severe("looping: " +
	    // tempPage.getName().toLowerCase().replace(" ", "-"));
	    // if (tempPage.getName().toLowerCase().replace(" ",
	    // "-").equals(History.getToken())) {
	    // currentPage = tempPage;
	    // view.displayCurrentPage();
	    // break;
	    // }
	    // }
	}
    }

    private void bind() {

	eventBus.addHandler(PageDeletedEvent.TYPE, new PageDeletedEventHandler() {
	    @Override
	    public void onPageDeleted(PageDeletedEvent event) {
		doPageDeleted();
	    }
	});

	eventBus.addHandler(PageUpdatedEvent.TYPE, new PageUpdatedEventHandler() {
	    @Override
	    public void onPageUpdated(PageUpdatedEvent event) {
		logger.log(Level.FINE, "onPageUpdated: ");
		doPageUpdated();
	    }
	});

	eventBus.addHandler(NavigationPagesLoadedEvent.TYPE, new NavigationPagesLoadedEventHandler() {
	    @Override
	    public void onNavigationPagesLoaded(NavigationPagesLoadedEvent event) {
		doNavigationPagesLoaded(event.getNavigationPages());
	    }
	});

	eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
	    @Override
	    public void onCommentUpdated(CommentUpdatedEvent event) {
		if (ContentKindType.page_comment.equals(event.getUpdatedContent().getKind())) {
		    // TODO load commented page instead of home. #66
		    History.back();
		    // view.displayCurrentPageData();
		    // Utils.setHistoryTokenIfNeeded(PresenterName.home);
		    // Utils.setHistoryTokenIfNeeded(PresenterName.page);
		}
	    }
	});
	eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
	    @Override
	    public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
		view.refreshControlsVisibility();
	    }
	});
    }

    @Override
    public Presenter getPageSpecificPresenter(List<Object> navigationPages) {
	Presenter result = null;
	PageDTO pageDTO = findPageByDisplayName(navigationPages, History.getToken());
	if (pageDTO != null) {
	    logger.log(Level.FINE, "pageDTO: " + pageDTO.getPageId() + ", name: " + pageDTO.getKind().name());
	    result = LocalStorage.getInstance().getPresenter(pageDTO.getKind().name());
	}
	logger.log(Level.FINE, "result: " + result);
	return result;
    }

    @Override
    public void displayMainPanel() {
	view.displayMainPanel();
    }

    @Override
    public void displayContextPanel(String presenterName) {
	view.displayContextPanel(LocalStorage.getInstance().getPresenter(presenterName));
    }

    @Override
    public void displayStatusMessage(String statusMessage) {
	StatusPresenter statusPresenter = (StatusPresenter) LocalStorage.getInstance().getPresenter(
		PresenterName.status.name());
	statusPresenter.setStatusMessage(statusMessage);
	view.displayContextPanel(statusPresenter);
    }

    @Override
    public String getPageDisplayName(PageDTO pageDTO) {
	logger.log(Level.FINE, "getPageDisplayName: " + pageDTO.getPageId());
	String result;
	ContentDTO contentDTO = pageDTO.getDescriptionContent();
	if (contentDTO != null) {
	    logger.log(Level.FINE, "contentDTO != null: " + contentDTO.getName());
	    result = contentDTO.getName();
	} else {
	    logger.log(Level.FINE, "contentDTO == null: " + pageDTO.getKind().name());
	    result = pageDTO.getKind().name();
	}
	logger.log(Level.FINE, "getPageDisplayName: " + contentDTO + ", " + result);
	return result;
    }

    private PageDTO findPageByDisplayName(List<Object> navigationData, String displayName) {
	logger.log(Level.FINE, "findPageByDisplayName: " + displayName);
	if (navigationData != null) {
	    logger.log(Level.FINE, "searching: " + History.getToken());
	    for (Object navigationEntry : navigationData) {
		if (navigationEntry != null) {
		    if (navigationEntry instanceof List) {
			PageDTO sublistResult = findPageByDisplayName((List<Object>) navigationEntry, displayName);
			if (sublistResult != null) {
			    logger.log(Level.FINE, "found: " + sublistResult.getPageId());
			    return sublistResult;
			}
		    } else if (navigationEntry instanceof PageDTO) {
			PageDTO tempPage = (PageDTO) navigationEntry;
			if (getPageDisplayName(tempPage).equalsIgnoreCase(displayName)) {
			    logger.log(Level.FINE, "found: " + tempPage.getPageId());
			    return tempPage;
			}
		    }
		}
	    }
	}
	logger.log(Level.FINE, "found: null");
	return null;
    }

    private void displayPageSpecific() {
	logger.log(Level.FINE, "displayPageSpecific: ");
	if ((currentPage == null)
		|| (!currentPage.getDescriptionContent().getKind().name().equalsIgnoreCase(History.getToken()))) {
	    logger.log(Level.FINE, "getNavigationPages call");
	    List<Object> navigationPages = LocalStorage.getInstance().getNavigationPages();
	    if (navigationPages != null) {
		logger.log(Level.FINE, "navigationPages != null: ");
		pageSpecificPresenter = getPageSpecificPresenter(navigationPages);
		findCurrentPage(navigationPages);
		view.displayPageSpecificPanel(navigationPages);
	    }
	}
    }

    @Override
    public void displayPageSpecificList() {
	view.displayPageSpecificList(LocalStorage.getInstance().getPresenter(PageKindType.lists.name()));
    }
}
