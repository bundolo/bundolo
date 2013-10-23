package org.bundolo.client.presenter;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.comment.CommentUpdatedEvent;
import org.bundolo.client.event.comment.CommentUpdatedEventHandler;
import org.bundolo.client.event.contest.AddContestEvent;
import org.bundolo.client.event.contest.ContestUpdatedEvent;
import org.bundolo.client.event.contest.ContestUpdatedEventHandler;
import org.bundolo.client.event.contest.EditContestEvent;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEvent;
import org.bundolo.client.event.user.UserAccessRightsUpdatedEventHandler;
import org.bundolo.client.view.content.ContentViewImpl;
import org.bundolo.client.view.contest.ContestView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.ItemListDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ItemListNameType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContestServiceAsync;
import org.bundolo.shared.services.ItemListServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class ContestPresenter implements Presenter, ContestView.Presenter {
	
	private static final Logger logger = Logger.getLogger(ContestPresenter.class.getName());

	private final HandlerManager eventBus;
	private final ContestView view;
	private ItemListDTO contestsItemListDTO;
	private ContestDTO contestDTO;
	private final ContestServiceAsync contestService;
	private final ItemListServiceAsync itemListService;
	private ContentPresenter contestContentPresenter;

	public ContestPresenter(ContestServiceAsync contestService, ItemListServiceAsync itemListService, HandlerManager eventBus, ContestView view) {
		this.contestService = contestService;
		this.itemListService = itemListService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
		ContentViewImpl contentViewImpl = new ContentViewImpl();
		contestContentPresenter = new ContentPresenter(LocalStorage.getInstance().getContentService(), eventBus, contentViewImpl);
	}

	@Override
	public void go(final HasWidgets container) {
		logger.log(Level.FINE, "adding ContestView");
		container.add(view.asWidget());
		itemListService.findItemListByName(ItemListNameType.new_contests.getItemListName(), null, Arrays.asList(Constants.DEFAULT_LOCALE), new AsyncCallback<ItemListDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error getting list: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.database_error)));
			}
			@Override
			public void onSuccess(ItemListDTO result) {
				contestsItemListDTO = result;
				view.displayContests();
			}}
		);
	}
	
	private void doContestUpdated(ContestDTO contestDTO) {
		setContestDTO(contestDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.contests);
	}

	private void bind() {
		eventBus.addHandler(ContestUpdatedEvent.TYPE, new ContestUpdatedEventHandler() {
			public void onContestUpdated(ContestUpdatedEvent event) {
				doContestUpdated(event.getUpdatedContest());
			}
		});
		eventBus.addHandler(CommentUpdatedEvent.TYPE, new CommentUpdatedEventHandler() {
			public void onCommentUpdated(CommentUpdatedEvent event) {
				if (ContentKindType.contest_comment.equals(event.getUpdatedContent().getKind())) {
					view.displayContest();
					Utils.setHistoryTokenIfNeeded(PresenterName.contests);
				}
			}
		});
		eventBus.addHandler(UserAccessRightsUpdatedEvent.TYPE, new UserAccessRightsUpdatedEventHandler() {
			public void onUserAccessRightsUpdated(UserAccessRightsUpdatedEvent event) {
				view.refreshControlsVisibility();
			}
		});
	}

	@Override
	public ItemListDTO getContestsItemListDTO() {
		return contestsItemListDTO;
	}

	@Override
	public void setContestDTO(ContestDTO contestDTO) {
		logger.log(Level.WARNING, "setContestDTO: " + contestDTO.getDescriptionContent().getName());
		this.contestDTO = contestDTO;
		view.displayContest();
	}

	@Override
	public ContestDTO getContestDTO() {
		return contestDTO;
	}

	@Override
	public ContentPresenter getContestContentPresenter() {
		return contestContentPresenter;
	}
	
	@Override
	public void onAddContestButtonClicked() {
		LocalStorage.getInstance().getPresenter(PresenterName.addContest.name());
		eventBus.fireEvent(new AddContestEvent());
	}

	@Override
	public void onDeleteContestButtonClicked() {
		if ((contestDTO != null) && (contestDTO.getContestId() != null)) {
			deleteCurrentContest();
		}
	}

	@Override
	public void onEditContestButtonClicked() {
		if ((contestDTO != null) && (contestDTO.getContestId() != null)) {
			LocalStorage.getInstance().getPresenter(PresenterName.editContest.name());
			eventBus.fireEvent(new EditContestEvent(contestDTO));
		}
	}
	
	private void deleteCurrentContest() {
		contestService.deleteContest(contestDTO.getContestId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Error deleting contest: ", caught);
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_deleting_failed)));
			}
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_deleted)));
			}}
		);
	}

	@Override
	public void reset(int level) {
		if (level > 0) {
			contestDTO = null;
		}
		view.reset(level);
	}
}