package org.bundolo.server.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.bundolo.server.model.Contest;
import org.bundolo.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;


@Repository("contestDAO")
public class ContestDAO extends JpaDAO<Long, Contest> {
	
	private static final Logger logger = Logger.getLogger(ContestDAO.class.getName());
	
	@Autowired
	@PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
	
	@SuppressWarnings("unchecked")
	public List<Contest> findItemListContests(final String queryString, final Integer start, final Integer end) {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				Query q = em.createQuery(queryString);
				q.setFirstResult(start);
				q.setMaxResults(end - start + 1);
				return q.getResultList();
			}
		});

		return (List<Contest>) res;
	}
	
	@SuppressWarnings("unchecked")
	public Integer findItemListContestsCount(final String queryString) {
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