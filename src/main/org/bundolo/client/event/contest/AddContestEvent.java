package org.bundolo.client.event.contest;

import com.google.gwt.event.shared.GwtEvent;

public class AddContestEvent extends GwtEvent<AddContestEventHandler> {
	public static Type<AddContestEventHandler> TYPE = new Type<AddContestEventHandler>();

	@Override
	public Type<AddContestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddContestEventHandler handler) {
		handler.onAddContest(this);
	} 
}
