package org.bundolo.client.event.contest;

import org.bundolo.shared.model.ContestDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditContestEvent extends GwtEvent<EditContestEventHandler>{
	public static Type<EditContestEventHandler> TYPE = new Type<EditContestEventHandler>();
	private final ContestDTO contestDTO;

	public EditContestEvent(ContestDTO contestDTO) {
		this.contestDTO = contestDTO;
	}

	public ContestDTO getContestDTO() {
		return contestDTO;
	}

	@Override
	public Type<EditContestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditContestEventHandler handler) {
		handler.onEditContest(this);
	}
}
