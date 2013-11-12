package org.bundolo.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage.PresenterName;
import org.bundolo.client.presenter.PagePresenter;
import org.bundolo.client.presenter.Presenter;
import org.bundolo.shared.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter, ValueChangeHandler<String> {

    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    private HasWidgets container;

    public AppController() {
	logger.log(Level.FINE, "AppController start");
	bind();
    }

    private void bind() {
	logger.log(Level.FINE, "bind start");
	History.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
	logger.log(Level.FINE, "onValueChange start");
	String token = event.getValue();

	if (token != null) {
	    logger.log(Level.WARNING, "token: " + token);

	    if (token.equals(PresenterName.addPage.name()) || token.equals(PresenterName.editPage.name())
		    || token.equals(PresenterName.addComment.name()) || token.equals(PresenterName.editComment.name())
		    || token.equals(PresenterName.user.name()) || token.equals(PresenterName.addUser.name())
		    || token.equals(PresenterName.editUser.name()) || token.equals(PresenterName.addList.name())
		    || token.equals(PresenterName.editList.name()) || token.equals(PresenterName.addText.name())
		    || token.equals(PresenterName.editText.name()) || token.equals(PresenterName.addConnection.name())
		    || token.equals(PresenterName.editConnection.name())
		    || token.equals(PresenterName.addContest.name()) || token.equals(PresenterName.editContest.name())
		    || token.equals(PresenterName.addEpisode.name()) || token.equals(PresenterName.editEpisode.name())
		    || token.equals(PresenterName.addForumPost.name())
		    || token.equals(PresenterName.editForumPost.name()) || token.equals(PresenterName.addNews.name())
		    || token.equals(PresenterName.editNews.name()) || token.equals(PresenterName.addSerial.name())
		    || token.equals(PresenterName.editSerial.name()) || token.equals(PresenterName.addMessage.name())
		    || token.equals(PresenterName.editLabels.name())) {
		((PagePresenter) LocalStorage.getInstance().getPresenter(PresenterName.page.name()))
			.displayContextPanel(token);
	    } else {
		logger.log(Level.FINE, "navigating to: " + token);
		GWT.runAsync(new RunAsyncCallback() {
		    @Override
		    public void onFailure(Throwable caught) {
			logger.log(Level.SEVERE, "onFailure: ", caught);
		    }

		    @Override
		    public void onSuccess() {
			LocalStorage.getInstance().getPresenter(PresenterName.page.name()).go(container);
		    }
		});
	    }
	}
    }

    @Override
    public void go(final HasWidgets container) {
	logger.log(Level.FINE, "go start");
	this.container = container;
	if ("".equals(History.getToken())) {
	    Utils.setHistoryTokenIfNeeded(PresenterName.home);
	} else {
	    History.fireCurrentHistoryState();
	}
    }

}
