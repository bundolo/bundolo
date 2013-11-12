package org.bundolo.client.view.content;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bundolo.client.LocalStorage;
import org.bundolo.client.widget.RaphaelButtonWidget;
import org.bundolo.shared.model.enumeration.LabelType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EditLabelsViewImpl extends Composite implements EditLabelsView {

    private static final Logger logger = Logger.getLogger(EditLabelsViewImpl.class.getName());

    @UiTemplate("EditLabelsView.ui.xml")
    interface EditLabelsViewUiBinder extends UiBinder<Widget, EditLabelsViewImpl> {
    }

    private static EditLabelsViewUiBinder uiBinder = GWT.create(EditLabelsViewUiBinder.class);

    @UiField
    HTMLPanel labelsContainer;

    @UiField
    RaphaelButtonWidget saveLabelsButton;

    @UiField
    RaphaelButtonWidget cancelLabelsButton;

    private Presenter presenter;

    private final Map<LabelType, TextBox> labelValues = new HashMap<LabelType, TextBox>();
    private final Map<LabelType, Label> labelLabels = new HashMap<LabelType, Label>();

    public EditLabelsViewImpl() {
	initWidget(uiBinder.createAndBindUi(this));
	createLabelFields();
    }

    @Override
    public void setPresenter(Presenter presenter) {
	this.presenter = presenter;
    }

    @UiHandler("saveLabelsButton")
    void onSaveLabelsButtonClicked(ClickEvent event) {
	if (presenter != null) {
	    presenter.onSaveLabelsButtonClicked();
	}
    }

    @UiHandler("cancelLabelsButton")
    void onCancelLabelsButtonClicked(ClickEvent event) {
	if (presenter != null) {
	    presenter.onCancelLabelsButtonClicked();
	}
    }

    @Override
    public Widget asWidget() {
	if (presenter != null) {
	}
	return this;
    }

    @Override
    public Map<String, String> getLabels() {
	// return only modified labels
	Map<String, String> result = new HashMap<String, String>();
	for (LabelType labelType : LabelType.values()) {
	    TextBox textBox = labelValues.get(labelType);
	    String newLabelValue = textBox.getValue();
	    String existingValue = LocalStorage.getInstance().getMessageResource().getLabel(labelType);
	    if (!existingValue.equals(newLabelValue)) {
		result.put(labelType.getLabelName(), newLabelValue);
	    }
	}
	return result;
    }

    private void createLabelFields() {
	for (LabelType labelType : LabelType.values()) {
	    Label newLabel = new Label(labelType.getLabelName());
	    labelLabels.put(labelType, newLabel);
	    labelsContainer.add(newLabel);
	    TextBox newValue = new TextBox();
	    newValue.setValue(LocalStorage.getInstance().getMessageResource().getLabel(labelType));
	    labelValues.put(labelType, newValue);
	    labelsContainer.add(newValue);
	}
    }

}