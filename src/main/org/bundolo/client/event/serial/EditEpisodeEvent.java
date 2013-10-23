package org.bundolo.client.event.serial;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditEpisodeEvent extends GwtEvent<EditEpisodeEventHandler>{
	public static Type<EditEpisodeEventHandler> TYPE = new Type<EditEpisodeEventHandler>();
	private final ContentDTO contentDTO;

	public EditEpisodeEvent(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public Type<EditEpisodeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditEpisodeEventHandler handler) {
		handler.onEditEpisode(this);
	}
}
