package org.bundolo.server.dao;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.bundolo.server.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("ratingDAO")
public class RatingDAO extends JpaDAO<Long, Rating> {
	
	private static final Logger logger = Logger.getLogger(RatingDAO.class.getName());
	
	@Autowired
	@PersistenceUnit(unitName = "BundoloPostgresPersistenceUnit")
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
	
}