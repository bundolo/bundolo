package org.bundolo.client.event.forum;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditForumPostEvent extends GwtEvent<EditForumPostEventHandler>{
	public static Type<EditForumPostEventHandler> TYPE = new Type<EditForumPostEventHandler>();
	private final ContentDTO contentDTO;

	public EditForumPostEvent(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public Type<EditForumPostEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditForumPostEventHandler handler) {
		handler.onEditForumPost(this);
	}
}
