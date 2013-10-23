package org.bundolo.shared.services;

import java.util.List;

import org.bundolo.shared.model.ConnectionDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bundoloServices/connectionService")
public interface ConnectionService extends RemoteService {
	
	public ConnectionDTO findConnection(Long connectionId);
	public Long saveConnection(ConnectionDTO connectionDTO) throws Exception;
	public void updateConnection(ConnectionDTO connectionDTO) throws Exception;
	public void deleteConnection(Long connectionId) throws Exception;

	public List<ConnectionDTO> findItemListConnections(String query, Integer start, Integer end) throws Exception;
	public Integer findItemListConnectionsCount(String query) throws Exception;
}
