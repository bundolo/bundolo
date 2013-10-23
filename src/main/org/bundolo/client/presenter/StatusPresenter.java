package org.bundolo.client.presenter;

import java.util.logging.Logger;

import org.bundolo.client.view.page.StatusView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class StatusPresenter implements Presenter, StatusView.Presenter {
	
	private static final Logger logger = Logger.getLogger(StatusPresenter.class.getName());

	private final HandlerManager eventBus;
	private final StatusView view;

	public StatusPresenter(HandlerManager eventBus, StatusView view) {
		//TODO we might not need this status functionality at all
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		bind();
	}

	@Override
	public void go(final HasWidgets container) {
		container.add(view.asWidget());
	}

	private void bind() {

	}

	@Override
	public void onContinueButtonClicked() {
		//TODO
	}

	@Override
	public void setStatusMessage(String statusMessage) {
		view.setStatusMessage(statusMessage);
	}
}