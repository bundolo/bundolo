package org.bundolo.client.event.serial;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EpisodeUpdatedEvent extends GwtEvent<EpisodeUpdatedEventHandler>{
	public static Type<EpisodeUpdatedEventHandler> TYPE = new Type<EpisodeUpdatedEventHandler>();
	private final ContentDTO updatedContent;

	public EpisodeUpdatedEvent(ContentDTO updatedContent) {
		this.updatedContent = updatedContent;
	}

	public ContentDTO getUpdatedContent() {
		return updatedContent;
	}

	@Override
	public Type<EpisodeUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EpisodeUpdatedEventHandler handler) {
		handler.onEpisodeUpdated(this);
	}
}