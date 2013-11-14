package org.bundolo.server.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.bundolo.server.model.UserProfile;
import org.bundolo.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;

@Repository("userProfileDAO")
public class UserProfileDAO extends JpaDAO<Long, UserProfile> {

    private static final Logger logger = Logger.getLogger(UserProfileDAO.class.getName());

    @Autowired
    @PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
    EntityManagerFactory entityManagerFactory;

    @PostConstruct
    public void init() {
	super.setEntityManagerFactory(entityManagerFactory);
    }

    @SuppressWarnings("unchecked")
    public UserProfile findByField(final String field, final String value) {
	Object res = getJpaTemplate().execute(new JpaCallback() {

	    @Override
	    public Object doInJpa(EntityManager em) throws PersistenceException {
		String queryString = "SELECT u FROM " + entityClass.getName() + " u";
		queryString += " WHERE " + field + " " + ((value == null) ? "IS NULL" : "='" + value + "'");
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
	return (UserProfile) res;
    }

    @SuppressWarnings("unchecked")
    public List<UserProfile> findItemListUsers(final String queryString, final Integer start, final Integer end) {
	Object res = getJpaTemplate().execute(new JpaCallback() {
	    @Override
	    public Object doInJpa(EntityManager em) throws PersistenceException {
		// String modifiedQueryString = queryString + " LIMIT " + (end -
		// start + 1) + " OFFSET " + start;
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
		Query q = em.createQuery(queryString);
		q.setFirstResult(start);
		q.setMaxResults(end - start + 1);
		return q.getResultList();
	    }
	});

	return (List<UserProfile>) res;
    }

    @SuppressWarnings("unchecked")
    public Integer findItemListUsersCount(final String queryString) {
	Object res = getJpaTemplate().execute(new JpaCallback() {
	    @Override
	    public Object doInJpa(EntityManager em) throws PersistenceException {
		String modifiedQueryString = queryString.replace("SELECT u", "SELECT COUNT(u)");
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