package org.bundolo.client.event.connection;

import org.bundolo.shared.model.ConnectionDTO;

import com.google.gwt.event.shared.GwtEvent;

public class EditConnectionEvent extends GwtEvent<EditConnectionEventHandler>{
	public static Type<EditConnectionEventHandler> TYPE = new Type<EditConnectionEventHandler>();
	private final ConnectionDTO connectionDTO;

	public EditConnectionEvent(ConnectionDTO connectionDTO) {
		this.connectionDTO = connectionDTO;
	}

	public ConnectionDTO getConnectionDTO() {
		return connectionDTO;
	}

	@Override
	public Type<EditConnectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditConnectionEventHandler handler) {
		handler.onEditConnection(this);
	}
}
