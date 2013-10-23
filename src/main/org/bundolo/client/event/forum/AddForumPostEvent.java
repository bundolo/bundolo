package org.bundolo.client.event.forum;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class AddForumPostEvent extends GwtEvent<AddForumPostEventHandler> {
	public static Type<AddForumPostEventHandler> TYPE = new Type<AddForumPostEventHandler>();
	
	private final ContentDTO parentContentDTO;
	
	public AddForumPostEvent(ContentDTO parentContentDTO) {
		this.parentContentDTO = parentContentDTO;
	}

	public ContentDTO getParentContentDTO() {
		return parentContentDTO;
	}

	@Override
	public Type<AddForumPostEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddForumPostEventHandler handler) {
		handler.onAddForumPost(this);
	} 
}
