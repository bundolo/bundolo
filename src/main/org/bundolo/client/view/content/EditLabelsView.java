package org.bundolo.client.view.content;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
public interface EditLabelsView {

	public interface Presenter {
		void onSaveLabelsButtonClicked();
		void onCancelLabelsButtonClicked();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	Map<String, String> getLabels();
}