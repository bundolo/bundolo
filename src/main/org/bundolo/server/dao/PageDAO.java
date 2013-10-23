package org.bundolo.server.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.bundolo.server.model.Page;
import org.bundolo.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;


@Repository("pageDAO")
public class PageDAO extends JpaDAO<Long, Page> {
	
	private static final Logger logger = Logger.getLogger(PageDAO.class.getName());

	@Autowired
	@PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
	EntityManagerFactory entityManagerFactory;

	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}

	@SuppressWarnings("unchecked")
	public List<Page> findPages(final Long parentPageId, final boolean justActive) {
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "findPages parentPageId: " + parentPageId + ", justActive: " + justActive);
		Object res = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT p FROM " + entityClass.getName() + " p";
				queryString += " WHERE parentPageId " + ((parentPageId == null)?"IS NULL":"=" + parentPageId);
				if (justActive) {
					queryString += " AND pageStatus = 'active'";
				}
				queryString += " ORDER BY displayOrder";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				
				Query q = em.createQuery(queryString);
				return q.getResultList();
			}

		});

		return (List<Page>) res;
	}
	
	@SuppressWarnings("unchecked")
	public Integer findPagesCount(final Long parentPageId) {
		Object res = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT COUNT(p) FROM " + entityClass.getName() + " p";
				queryString += " WHERE parentPageId " + ((parentPageId == null)?"IS NULL":"=" + parentPageId);
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				Query q = em.createQuery(queryString);
				return q.getSingleResult();
			}

		});

		if (res == null) {
			res = new Long(0);
		}
		return ((Long) res).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public Page findNextPage(final Long previousPageId) {
		Object res = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT p FROM " + entityClass.getName() + " p";
				if (previousPageId != null) {
					Page previousPage = findById(previousPageId);
					if (previousPage != null) {
						queryString += " WHERE p.parentPageId " + ((previousPage.getParentPageId() == null)?"IS NULL":"= " + previousPage.getParentPageId());
						queryString += " AND p.displayOrder > " + previousPage.getDisplayOrder();
					} else {
						queryString += " WHERE p.parentPageId IS NULL";
					}
				} else {
					queryString += " WHERE p.parentPageId IS NULL";
				}
				queryString += " ORDER BY p.displayOrder";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				
				Query q = em.createQuery(queryString);
				q.setMaxResults(1);
				List resultList = q.getResultList();
				if (resultList != null && resultList.size() > 0) {
					return resultList.get(0);
				} else {
					return null;
				}
			}

		});

		return (Page) res;
	}
	
	@SuppressWarnings("unchecked")
	public Page findHomePage() {
		
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "findHomePage");
		Object res = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT p FROM " + entityClass.getName() + " p";
				queryString += " WHERE p.parentPageId IS NULL";
				queryString += " ORDER BY p.displayOrder";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				
				Query q = em.createQuery(queryString);
				q.setMaxResults(1);
				List resultList = q.getResultList();
				if (resultList != null && resultList.size() > 0) {
					return resultList.get(0);
				} else {
					return null;
				}
			}

		});

		return (Page) res;
	}

}