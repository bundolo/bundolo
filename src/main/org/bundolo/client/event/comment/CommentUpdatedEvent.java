package org.bundolo.client.event.comment;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class CommentUpdatedEvent extends GwtEvent<CommentUpdatedEventHandler>{
	public static Type<CommentUpdatedEventHandler> TYPE = new Type<CommentUpdatedEventHandler>();
	private final ContentDTO updatedContent;

	public CommentUpdatedEvent(ContentDTO updatedContent) {
		this.updatedContent = updatedContent;
	}

	public ContentDTO getUpdatedContent() {
		return updatedContent;
	}

	@Override
	public Type<CommentUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CommentUpdatedEventHandler handler) {
		handler.onCommentUpdated(this);
	}
}