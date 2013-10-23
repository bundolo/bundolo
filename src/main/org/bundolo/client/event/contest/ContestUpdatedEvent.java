package org.bundolo.client.event.contest;

import org.bundolo.shared.model.ContestDTO;

import com.google.gwt.event.shared.GwtEvent;

public class ContestUpdatedEvent extends GwtEvent<ContestUpdatedEventHandler>{
	public static Type<ContestUpdatedEventHandler> TYPE = new Type<ContestUpdatedEventHandler>();
	private final ContestDTO updatedContest;

	public ContestUpdatedEvent(ContestDTO updatedContest) {
		this.updatedContest = updatedContest;
	}

	public ContestDTO getUpdatedContest() {
		return updatedContest;
	}

	@Override
	public Type<ContestUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ContestUpdatedEventHandler handler) {
		handler.onContestUpdated(this);
	}
}