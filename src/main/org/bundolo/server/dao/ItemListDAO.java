package org.bundolo.server.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.bundolo.server.model.ItemList;
import org.bundolo.shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;


@Repository("itemListDAO")
public class ItemListDAO extends JpaDAO<Long, ItemList> {
	
	private static final Logger logger = Logger.getLogger(ItemListDAO.class.getName());

	@Autowired
	@PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
	EntityManagerFactory entityManagerFactory;

	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
	
	@SuppressWarnings("unchecked")
	public ItemList findByName(final String name, final String authorUsername) {
		Object res = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT l FROM " + entityClass.getName() + " l, Content c";
				queryString += " WHERE c.name " + ((name == null)?"IS NULL":"='" + name + "'");
				queryString += " AND c.contentId = l.descriptionContentId";
				queryString += " AND l.authorUsername " + ((authorUsername == null)?"IS NULL":"=" + authorUsername);
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
		return (ItemList) res;
	}

}