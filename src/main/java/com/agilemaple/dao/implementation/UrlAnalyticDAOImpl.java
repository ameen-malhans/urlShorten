package com.agilemaple.dao.implementation;

import java.util.Optional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.agilemaple.dao.UrlAnalyticDAO;
import com.agilemaple.model.UrlAnalytic;


@Repository
public class UrlAnalyticDAOImpl implements UrlAnalyticDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(UrlAnalyticDAOImpl.class);
    @Autowired
	private SessionFactory sessionFactory; //session library of hibernate
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public Long saveAnalytic(UrlAnalytic urlAnalytic) {
		Session session = this.sessionFactory.getCurrentSession();
		Long id = (Long) session.save(urlAnalytic);
		logger.info("UrlAnalytic saved successfully, UrlAnalytic Details="+id);
		return id;
	}

	@Override
	public void updateAnalytic(UrlAnalytic urlAnalytic) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("update UrlAnalytic analytic set analytic.count = :count where analytic.id =:id");
		Integer updatedCount = urlAnalytic.getCount();
		query.setParameter("count", ++updatedCount );
		query.setParameter("id", urlAnalytic.getId());
		query.executeUpdate();
	}

	@Override
	public Optional<UrlAnalytic> findAnalyticById(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		UrlAnalytic urlAnalytic = (UrlAnalytic) session.get(UrlAnalytic.class, new Long(id));
		logger.info("UrlAnalytic loaded successfully, UrlAnalytic details="+urlAnalytic);
		return Optional.ofNullable(urlAnalytic);
	}


}
