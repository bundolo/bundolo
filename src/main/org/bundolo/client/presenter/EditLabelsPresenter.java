package org.bundolo.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.event.notification.DisplayNotificationEvent;
import org.bundolo.client.view.content.EditLabelsView;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.enumeration.LabelType;
import org.bundolo.shared.services.ContentServiceAsync;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class EditLabelsPresenter implements Presenter, EditLabelsView.Presenter {

    private static final Logger logger = Logger.getLogger(EditLabelsPresenter.class.getName());

    private final ContentServiceAsync contentService;
    private final HandlerManager eventBus;
    private final EditLabelsView view;

    public EditLabelsPresenter(ContentServiceAsync contentService, HandlerManager eventBus, EditLabelsView view) {
	this.contentService = contentService;
	this.eventBus = eventBus;
	this.view = view;
	this.view.setPresenter(this);
	bind();
    }

    @Override
    public void onSaveLabelsButtonClicked() {
	// TODO see about validation
	doSave();
    }

    @Override
    public void onCancelLabelsButtonClicked() {
	doEditLabelsCancelled();
    }

    @Override
    public void go(final HasWidgets container) {
	logger.log(Level.FINE, "go");
	// TODO this check is performed before user is logged in, if he just
	// came and has session still open
	// TODO in case of event fired, page will be displayed and then replaced
	// with previous one. probably because preparing previous page takes
	// some time
	if (!LocalStorage.getInstance().isUserLoggedIn()) {
	    History.back();
	    eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
		    .getLabel(LabelType.no_permission)));
	}
	container.clear();
	container.add(view.asWidget());
    }

    private void doSave() {
	logger.log(Level.FINE, "doSave start");
	// TODO locale is currently hard coded
	contentService.saveLabels(view.getLabels(), new AsyncCallback<Void>() {
	    @Override
	    public void onFailure(Throwable caught) {
		logger.log(Level.SEVERE, "Error saving labels: ", caught);
		eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			.getLabel(LabelType.labels_saving_failed)));
	    }

	    @Override
	    public void onSuccess(Void result) {
		logger.log(Level.FINE, "saveLabels onSuccess");
		// TODO reload labels perhaps
		Utils.setHistoryTokenIfNeeded(PresenterName.home);
		eventBus.fireEvent(new DisplayNotificationEvent(LocalStorage.getInstance().getMessageResource()
			.getLabel(LabelType.labels_saved)));
	    }
	});
    }

    private void doEditLabelsCancelled() {
	History.back();
    }

    private void bind() {
    }
}