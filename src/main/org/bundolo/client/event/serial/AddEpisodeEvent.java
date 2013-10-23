package org.bundolo.client.event.serial;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class AddEpisodeEvent extends GwtEvent<AddEpisodeEventHandler> {
	public static Type<AddEpisodeEventHandler> TYPE = new Type<AddEpisodeEventHandler>();

	private final ContentDTO parentContentDTO;

	public AddEpisodeEvent(ContentDTO parentContentDTO) {
		this.parentContentDTO = parentContentDTO;
	}

	public ContentDTO getParentContentDTO() {
		return parentContentDTO;
	}
	
	@Override
	public Type<AddEpisodeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddEpisodeEventHandler handler) {
		handler.onAddEpisode(this);
	} 
}
