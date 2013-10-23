package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.contest.AddContestEvent;
import org.bundolo.client.event.contest.AddContestEventHandler;
import org.bundolo.client.event.contest.ContestUpdatedEvent;
import org.bundolo.client.event.contest.EditContestEvent;
import org.bundolo.client.event.contest.EditContestEventHandler;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.contest.EditContestView;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.ContestDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.model.enumeration.ContestKindType;
import org.bundolo.shared.model.enumeration.ContestStatusType;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContestServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import eu.maydu.gwt.validation.client.DefaultValidationProcessor;
import eu.maydu.gwt.validation.client.ValidationException;
import eu.maydu.gwt.validation.client.ValidationProcessor;

public class EditContestPresenter implements Presenter, EditContestView.Presenter {

	private static final Logger logger = Logger.getLogger(EditContestPresenter.class.getName());

	private ContestDTO contestDTO;
	private final ContestServiceAsync contestService;
	private final HandlerManager eventBus;
	private final EditContestView view;
	private ValidationProcessor validator;

	public EditContestPresenter(ContestServiceAsync contestService, HandlerManager eventBus, EditContestView view) {
		this.contestService = contestService;
		this.eventBus = eventBus;
		this.view = view;
		validator = new DefaultValidationProcessor(LocalStorage.getInstance().getValidationMessages());
		this.view.setPresenter(this);		
		bind();
	}

	@Override
	public void onSaveContestButtonClicked() {
		if (validator.validate()) {
			doSave();
		}
	}

	@Override
	public void onCancelContestButtonClicked() {
		doEditContestCancelled();
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
		contestDTO.setExpirationDate(view.getExpirationDate());
		contestDTO.getDescriptionContent().setName(view.getName());
		contestDTO.getDescriptionContent().setText(view.getDescription());
		if (contestDTO.getContestId() == null) {			
			contestDTO.setContestStatus(ContestStatusType.active);
			contestDTO.setKind(ContestKindType.general);
			contestDTO.getDescriptionContent().setContentStatus(ContentStatusType.active);
			
			contestService.saveContest(contestDTO, new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error saving contest: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_adding_failed)));
		            }
				}
				@Override
				public void onSuccess(Long result) {
					logger.log(Level.FINE, "doSave onSuccess");
					contestDTO.setContestId(result);					
					eventBus.fireEvent(new ContestUpdatedEvent(contestDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_added)));
				}
			});
		} else {
			contestService.updateContest(contestDTO, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					if(caught instanceof ValidationException) {
		                ValidationException ex = (ValidationException) caught;
		                logger.log(Level.FINE, "Server side validation failed: ", ex);
		                validator.processServerErrors(ex);
		            } else {
		            	logger.log(Level.SEVERE, "Error updating contest: ", caught);
		            	eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_update_failed)));
		            }
				}
				@Override
				public void onSuccess(Void result) {
					logger.log(Level.FINE, "updateContest onSuccess");
					eventBus.fireEvent(new ContestUpdatedEvent(contestDTO));
					eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource().getLabel(LabelType.contest_updated)));
				}
			});
		}
		
	}

	private void doEditContestCancelled() {
		History.back();
	}
	
	private void bind() {
		eventBus.addHandler(AddContestEvent.TYPE, new AddContestEventHandler() {
			public void onAddContest(AddContestEvent event) {
				doAddContest();
			}
		});

		eventBus.addHandler(EditContestEvent.TYPE, new EditContestEventHandler() {
			public void onEditContest(EditContestEvent event) {
				doEditContest(event.getContestDTO());
			}
		});
	}
	
	protected void doAddContest() {
		ContestDTO newContestDTO = new ContestDTO();
		ContentDTO newContestDescriptionContent = new ContentDTO();
		newContestDescriptionContent.setKind(ContentKindType.contest_description);
		newContestDTO.setDescriptionContent(newContestDescriptionContent);
		setContestDTO(newContestDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.addContest);
	}

	private void doEditContest(ContestDTO contestDTO) {
		setContestDTO(contestDTO);
		Utils.setHistoryTokenIfNeeded(PresenterName.editContest);
	}

	@Override
	public ContestDTO getContestDTO() {
		return contestDTO;
	}

	@Override
	public void setContestDTO(ContestDTO contestDTO) {
		this.contestDTO = contestDTO;
	}

	@Override
	public ValidationProcessor getValidator() {
		return validator;
	}
}
