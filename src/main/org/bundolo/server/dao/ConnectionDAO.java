package org.bundolo.server.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.bundolo.server.model.Connection;
import org.bundolo.server.model.Content;
import org.bundolo.server.model.Page;
import org.bundolo.server.model.UserProfile;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;


@Repository("connectionDAO")
public class ConnectionDAO extends JpaDAO<Long, Connection> {
	
	private static final Logger logger = Logger.getLogger(ConnectionDAO.class.getName());
	
	@Autowired
	@PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
	
	@SuppressWarnings("unchecked")
	public List<Connection> findItemListConnections(final String queryString, final Integer start, final Integer end) {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				Query q = em.createQuery(queryString);
				q.setFirstResult(start);
				q.setMaxResults(end - start + 1);
				return q.getResultList();
			}
		});

		return (List<Connection>) res;
	}
	
	@SuppressWarnings("unchecked")
	public Integer findItemListConnectionsCount(final String queryString) {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				String modifiedQueryString = queryString.replace("SELECT c", "SELECT COUNT(c)");
				int orderBySectionStart = modifiedQueryString.indexOf(" ORDER BY ");
				if (orderBySectionStart > -1) {
					modifiedQueryString = modifiedQueryString.substring(0, orderBySectionStart);
				}
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + modifiedQueryString);
				
				Query q = em.createQuery(modifiedQueryString);
				return q.getSingleResult();
			}
		});
		if (res == null) {
			res = new Long(0);
		}
		return ((Long) res).intValue();
	}
}