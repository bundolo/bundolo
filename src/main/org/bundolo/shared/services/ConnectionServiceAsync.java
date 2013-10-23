package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ConnectionDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface ConnectionServiceAsync {

	void findConnection(Long connectionId, AsyncCallback<ConnectionDTO> callback);

	void saveConnection(ConnectionDTO connectionDTO, AsyncCallback<Long> callback);

	void updateConnection(ConnectionDTO connectionDTO, AsyncCallback<Void> callback);

	void deleteConnection(Long connectionId, AsyncCallback<Void> callback);

	void findItemListConnections(String query, Integer start, Integer end, AsyncCallback<List<ConnectionDTO>> callback);

	void findItemListConnectionsCount(String query, AsyncCallback<Integer> callback);

}
