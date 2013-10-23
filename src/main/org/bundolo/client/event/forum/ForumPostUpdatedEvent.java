package org.bundolo.client.event.forum;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class ForumPostUpdatedEvent extends GwtEvent<ForumPostUpdatedEventHandler>{
	public static Type<ForumPostUpdatedEventHandler> TYPE = new Type<ForumPostUpdatedEventHandler>();
	private final ContentDTO updatedContent;

	public ForumPostUpdatedEvent(ContentDTO updatedContent) {
		this.updatedContent = updatedContent;
	}

	public ContentDTO getUpdatedContent() {
		return updatedContent;
	}

	@Override
	public Type<ForumPostUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ForumPostUpdatedEventHandler handler) {
		handler.onForumPostUpdated(this);
	}
}