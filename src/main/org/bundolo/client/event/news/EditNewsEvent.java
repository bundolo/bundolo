package org.bundolo.client.event.news;

import org.bundolo.shared.model.ContentDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditNewsEvent extends GwtEvent<EditNewsEventHandler>{
	public static Type<EditNewsEventHandler> TYPE = new Type<EditNewsEventHandler>();
	private final ContentDTO contentDTO;

	public EditNewsEvent(ContentDTO contentDTO) {
		this.contentDTO = contentDTO;
	}

	public ContentDTO getContentDTO() {
		return contentDTO;
	}

	@Override
	public Type<EditNewsEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditNewsEventHandler handler) {
		handler.onEditNews(this);
	}
}
