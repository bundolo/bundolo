package org.bundolo.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bundolo.server.SessionUtils;
import org.bundolo.server.dao.ConnectionDAO;
import org.bundolo.server.dao.ContentDAO;
import org.bundolo.server.model.Connection;
import org.bundolo.server.model.Content;
import org.bundolo.shared.model.ConnectionDTO;
import org.bundolo.shared.model.ContentDTO;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.bundolo.shared.model.enumeration.ContentStatusType;
import org.bundolo.shared.services.ConnectionService;
import org.bundolo.shared.services.ContentService;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("connectionService")
public class ConnectionServiceImpl implements ConnectionService {
	
	private static final Logger logger = Logger.getLogger(ConnectionServiceImpl.class.getName());

	@Autowired
	private ConnectionDAO connectionDAO;
	
	@Autowired
	private ContentDAO contentDAO;
	
	@Autowired
	private ContentService contentService;

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() {
	}
	
	@Override
	public ConnectionDTO findConnection(Long connectionId) {
		ConnectionDTO result = null;
		Connection connection = connectionDAO.findById(connectionId);
		if(connection != null) {
			result = DozerBeanMapperSingletonWrapper.getInstance().map(connection, ConnectionDTO.class);
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Long saveConnection(ConnectionDTO connectionDTO) throws Exception {
		Long result = null;
		Connection connection = null;
		if (connectionDTO.getConnectionId() != null) {
			connection = connectionDAO.findById(connectionDTO.getConnectionId());
		}
		if(connection == null) {
			if (connectionDTO.getDescriptionContent() != null) {
				Long contentId = contentService.saveContent(connectionDTO.getDescriptionContent());
				connection = new Connection(connectionDTO.getConnectionId(), connectionDTO.getAuthorUsername(), connectionDTO.getParentContentId(), contentId, connectionDTO.getKind(), new Date(), connectionDTO.getConnectionStatus(), connectionDTO.getEmail(), connectionDTO.getUrl());
				try {
					connectionDAO.persist(connection);
				} catch (Exception ex) {
					contentService.deleteContent(contentId);
					throw new Exception("db exception");
				}
				result = connection.getConnectionId();
			}
		}
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateConnection(ConnectionDTO connectionDTO) throws Exception {
		Connection connection = connectionDAO.findById(connectionDTO.getConnectionId());

		if(connection != null) {
			ContentDTO descriptionContent = connectionDTO.getDescriptionContent();
			if (descriptionContent != null) {
				if (descriptionContent.getContentId() == null) {
					contentService.saveContent(connectionDTO.getDescriptionContent());
				} else {
					contentService.updateContent(connectionDTO.getDescriptionContent());
				}
			}
			connection.setConnectionStatus(connectionDTO.getConnectionStatus());
			connection.setCreationDate(connectionDTO.getCreationDate());
			connection.setEmail(connectionDTO.getEmail());
			connection.setKind(connectionDTO.getKind());
			connection.setUrl(connectionDTO.getUrl());
			connectionDAO.merge(connection);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteConnection(Long connectionId) throws Exception {
		Connection connection = connectionDAO.findById(connectionId);
		if(connection != null) {
			connectionDAO.remove(connection);
		}
	}

	@Override
	public List<ConnectionDTO> findItemListConnections(String query, Integer start, Integer end) throws Exception {
		List<Connection> connections = connectionDAO.findItemListConnections(query, start, end);
		List<ConnectionDTO> connectionDTOs = new ArrayList<ConnectionDTO>();
		if (connections != null) {
			for (Connection connection : connections) {
				ConnectionDTO connectionDTO = DozerBeanMapperSingletonWrapper.getInstance().map(connection, ConnectionDTO.class);
				Content descriptionContent = contentDAO.findContentForLocale(connectionDTO.getDescriptionContentId(), ContentKindType.connection_description, SessionUtils.getUserLocale());
				if (descriptionContent != null) {
					connectionDTO.setDescriptionContent(DozerBeanMapperSingletonWrapper.getInstance().map(descriptionContent, ContentDTO.class));
				}
				connectionDTOs.add(connectionDTO);				
			}
		}
		return connectionDTOs;
	}

	@Override
	public Integer findItemListConnectionsCount(String query) throws Exception {
		return connectionDAO.findItemListConnectionsCount(query);
	}

}
