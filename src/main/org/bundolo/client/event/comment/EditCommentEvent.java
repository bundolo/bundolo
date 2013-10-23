package org.bundolo.client.event.comment;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditCommentEvent extends GwtEvent<EditCommentEventHandler>{
	public static Type<EditCommentEventHandler> TYPE = new Type<EditCommentEventHandler>();
	private final ContentDTO contentDTO;

	public EditCommentEvent(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public Type<EditCommentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditCommentEventHandler handler) {
		handler.onEditComment(this);
	}
}
