package org.bundolo.client.event.news;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class NewsUpdatedEvent extends GwtEvent<NewsUpdatedEventHandler>{
	public static Type<NewsUpdatedEventHandler> TYPE = new Type<NewsUpdatedEventHandler>();
	private final ContentDTO updatedContent;

	public NewsUpdatedEvent(ContentDTO updatedContent) {
		this.updatedContent = updatedContent;
	}

	public ContentDTO getUpdatedContent() {
		return updatedContent;
	}

	@Override
	public Type<NewsUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewsUpdatedEventHandler handler) {
		handler.onNewsUpdated(this);
	}
}