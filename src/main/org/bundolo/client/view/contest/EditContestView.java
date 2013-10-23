package org.bundolo.client.view.contest;

import java.util.Date;

import org.bundolo.shared.model.ContestDTO;

import com.google.gwt.user.client.ui.Widget;

import eu.maydu.gwt.validation.client.ValidationProcessor;
public interface EditContestView {

	public interface Presenter {
		void onSaveContestButtonClicked();
		void onCancelContestButtonClicked();
		ContestDTO getContestDTO();
		void setContestDTO(ContestDTO contestDTO);
		ValidationProcessor getValidator();
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
	String getName();
	String getDescription();
	Date getExpirationDate();
}