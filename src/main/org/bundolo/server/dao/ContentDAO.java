package org.bundolo.server.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.bundolo.server.model.Content;
import org.bundolo.server.model.Page;
import org.bundolo.shared.Constants;
import org.bundolo.shared.Utils;
import org.bundolo.shared.model.enumeration.ContentKindType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;


@Repository("contentDAO")
public class ContentDAO extends JpaDAO<Long, Content> {

	private static final Logger logger = Logger.getLogger(ContentDAO.class.getName());

	@Autowired
	@PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
	EntityManagerFactory entityManagerFactory;

	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}

	@SuppressWarnings("unchecked")
	public List<Content> findContents(final Long parentContentId, final ContentKindType kind, final String locale) {
		Object res = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT c FROM " + entityClass.getName() + " c";
				queryString += " WHERE parentContentId " + ((parentContentId == null)?"IS NULL":"=" + parentContentId);
				queryString += " AND kind " + ((kind == null)?"IS NULL":"='" + kind + "'");
				queryString += " AND locale " + ((locale == null)?"IS NULL":"='" + locale + "'");
				queryString += " ORDER BY creationDate";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);

				Query q = em.createQuery(queryString);
				return q.getResultList();
			}

		});

		return (List<Content>) res;
	}

	/*
	 * get child content if it's the right locale. if not, get parent content in the right locale. if not found, return null
	 */
	@SuppressWarnings("unchecked")
	public Content findContentForLocale(final Long contentId, final ContentKindType kind, final String locale) {
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "locale: " + locale);
		Content result = null;
		if (contentId != null && Utils.hasText(locale)) {
			result = findById(contentId);
			if (result != null && !locale.equals(result.getLocale())) {
				Object res = getJpaTemplate().execute(new JpaCallback() {

					public Object doInJpa(EntityManager em) throws PersistenceException {
						String queryString = "SELECT c FROM " + entityClass.getName() + " c";
						queryString += " WHERE parentContentId =" + contentId;
						queryString += " AND kind " + ((kind == null)?"IS NULL":"='" + kind + "'");
						queryString += " AND locale ='" + locale + "'";
						logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);

						Query q = em.createQuery(queryString);
						q.setMaxResults(1);
						List resultList = q.getResultList();
						if (resultList != null && resultList.size() > 0) {
							return resultList.get(0);
						} else {
							String queryStringChild = "SELECT c FROM " + entityClass.getName() + " c";
							queryStringChild += " WHERE contentId =" + contentId;
							queryString += " AND kind " + ((kind == null)?"IS NULL":"='" + kind + "'");
							//queryStringChild += " AND locale ='" + locale + "'";
							logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryStringChild: " + queryStringChild);

							Query qChild = em.createQuery(queryStringChild);
							qChild.setMaxResults(1);
							List resultListChild = qChild.getResultList();
							if (resultListChild != null && resultListChild.size() > 0) {
								return resultListChild.get(0);
							} else {
								return null;
							}
						}
					}

				});
				if (res == null) {
					result = null;
				} else {
					result = (Content) res;
				}
			}
		}
		return result;
	}

	/*
	 * get content by its name. kind and locale, without locale fallback. used for labels at the moment
	 */
	@SuppressWarnings("unchecked")
	public Content findContentForLocale(final String contentName, final ContentKindType kind, final String locale) {
		logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "locale: " + locale);
		Content result = null;
		if (Utils.hasText(contentName) && Utils.hasText(locale)) {
			Object res = getJpaTemplate().execute(new JpaCallback() {
				public Object doInJpa(EntityManager em) throws PersistenceException {
					String queryString = "SELECT c FROM " + entityClass.getName() + " c";
					queryString += " WHERE content_name =" + contentName;
					queryString += " AND kind " + ((kind == null)?"IS NULL":"='" + kind + "'");
					queryString += " AND locale ='" + locale + "'";
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
			if (res == null) {
				result = null;
			} else {
				result = (Content) res;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public String getDefaultLocale() {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT DISTINCT(c.locale) FROM " + entityClass.getName() + " c";
				queryString += " WHERE parentContentId IS NULL";
				queryString += " and kind='label'";
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
		return (String) res;
	}

	@SuppressWarnings("unchecked")
	public List<String> getNonDefaultLocales() {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT DISTINCT(c.locale) FROM " + entityClass.getName() + " c";
				queryString += " WHERE parentContentId IS NOT NULL";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				Query q = em.createQuery(queryString);
				return q.getResultList();
			}
		});
		return (List<String>) res;
	}

	@SuppressWarnings("unchecked")
	public List<String> getLocales() {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT DISTINCT(c.locale) FROM " + entityClass.getName() + " c";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				Query q = em.createQuery(queryString);
				return q.getResultList();
			}
		});
		return (List<String>) res;
	}

	@SuppressWarnings("unchecked")
	public List<Content> getLabelsForLocale(final String locale) {

		//idea is to get all entries translated for this locale and default language entries that haven't been translated
		//since union and except are not supported, query does not work
		//list joining was implemented instead of union, "not in" is used instead of "except"
		//native query was one of the options as well
		//select c.* from content c where c.parent_page_id is null and c.parent_content_id is not null and c.locale='sr'
		//union all (select c.* from content c where c.parent_page_id is null and c.parent_content_id is null except all
		//		select c1.* from content c, content c1 where c.parent_page_id is null and c.parent_content_id is not null and c.locale='sr' and c1.content_id=c.parent_content_id);

		Object res1 = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT c1 FROM " + entityClass.getName() + " c1";
				queryString += " WHERE c1.parentContentId IS NOT NULL";
				queryString += " AND c1.kind = 'label'";
				queryString += " AND c1.locale ='" + locale +"'";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);

				Query q = em.createQuery(queryString);
				return q.getResultList();
			}

		});

		Object res2 = getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "SELECT c FROM " + entityClass.getName() + " c";
				queryString += " WHERE c.parentContentId IS NULL";
				queryString += " AND c.kind = 'label'";
				queryString += " AND c.contentId NOT IN(";
				queryString += " SELECT c1.parentContentId FROM " + entityClass.getName() + " c1";
				queryString += " WHERE c1.parentContentId IS NOT NULL";
				queryString += " AND c1.kind = 'label'";
				queryString += " AND c1.locale ='" + locale +"')";
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);

				Query q = em.createQuery(queryString);
				return q.getResultList();
			}

		});
		List<Content> res = (List<Content>) res1;
		res.addAll((List<Content>) res2);
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Content> findItemListContents(final String queryString, final Integer start, final Integer end) {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				//String modifiedQueryString = queryString + " LIMIT " + (end - start + 1) + " OFFSET " + start;
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);
				Query q = em.createQuery(queryString);
				q.setFirstResult(start);
				q.setMaxResults(end - start + 1);
				return q.getResultList();
			}
		});

		return (List<Content>) res;
	}

	@SuppressWarnings("unchecked")
	public Integer findItemListContentsCount(final String queryString) {
		Object res = getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				String modifiedQueryString = queryString.replace("SELECT c", "SELECT COUNT(c)");
				int orderBySectionStart = modifiedQueryString.indexOf(" ORDER BY ");
				if (orderBySectionStart > -1) {
					modifiedQueryString = modifiedQueryString.substring(0, orderBySectionStart);
				}
				logger.log(Constants.SERVER_DEBUG_LOG_LEVEL, "queryString: " + queryString);

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