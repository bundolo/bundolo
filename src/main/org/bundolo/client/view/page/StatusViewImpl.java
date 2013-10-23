package org.bundolo.client.view.page;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StatusViewImpl extends Composite implements StatusView {

	private static final Logger logger = Logger.getLogger(StatusViewImpl.class.getName());

	@UiTemplate("StatusView.ui.xml")
	interface StatusViewUiBinder extends UiBinder<Widget, StatusViewImpl> {}
	private static StatusViewUiBinder uiBinder = GWT.create(StatusViewUiBinder.class);
	
	@UiField Label statusMessageLabel;
	@UiField Button continueButton;

	private Presenter presenter;

	public StatusViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public Widget asWidget() {
		return this;
	}
	
	@UiHandler("continueButton")
	void onContinueButtonClicked(ClickEvent event) {
		if (presenter != null) {
			presenter.onContinueButtonClicked();
		}
	}

	@Override
	public void setStatusMessage(String statusMessage) {
		statusMessageLabel.setText(statusMessage);
	}

}