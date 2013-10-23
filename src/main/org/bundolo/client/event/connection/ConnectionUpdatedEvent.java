package org.bundolo.client.event.connection;

import org.bundolo.shared.model.ConnectionDTO;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectionUpdatedEvent extends GwtEvent<ConnectionUpdatedEventHandler>{
	public static Type<ConnectionUpdatedEventHandler> TYPE = new Type<ConnectionUpdatedEventHandler>();
	private final ConnectionDTO updatedConnection;

	public ConnectionUpdatedEvent(ConnectionDTO updatedConnection) {
		this.updatedConnection = updatedConnection;
	}

	public ConnectionDTO getUpdatedConnection() {
		return updatedConnection;
	}

	@Override
	public Type<ConnectionUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ConnectionUpdatedEventHandler handler) {
		handler.onConnectionUpdated(this);
	}
}