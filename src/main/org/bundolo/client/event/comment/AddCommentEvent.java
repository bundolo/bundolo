package org.bundolo.client.event.comment;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class AddCommentEvent extends GwtEvent<AddCommentEventHandler> {
	public static Type<AddCommentEventHandler> TYPE = new Type<AddCommentEventHandler>();

	private final ContentDTO parentContentDTO;

	public AddCommentEvent(ContentDTO parentContentDTO) {
		this.parentContentDTO = parentContentDTO;
	}

	public ContentDTO getParentContentDTO() {
		return parentContentDTO;
	}

	@Override
	public Type<AddCommentEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddCommentEventHandler handler) {
		handler.onAddComment(this);
	} 
}
