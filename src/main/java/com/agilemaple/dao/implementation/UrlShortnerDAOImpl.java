package com.agilemaple.dao.implementation;

import com.agilemaple.dao.UrlShortnerDAO;
import com.agilemaple.model.UrlShortner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class UrlShortnerDAOImpl implements UrlShortnerDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(UrlShortnerDAOImpl.class);
    @Autowired
	private SessionFactory sessionFactory; //session library of hibernate
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public Long saveUrl(UrlShortner urlShortner) {
		Session session = this.sessionFactory.getCurrentSession();
		Long id = (Long) session.save(urlShortner);
		logger.info("UrlShortner saved successfully, UrlShortner Details="+id);
		return id;
	}
	
	@Override
	public Optional<UrlShortner> findeById(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		UrlShortner urlShortner = (UrlShortner) session.get(UrlShortner.class, new Long(id));
		logger.info("UrlShortner loaded successfully, UrlShortner details="+urlShortner);
		return Optional.ofNullable(urlShortner);
	}

	@Override
	public Optional<UrlShortner> findByOriginalUrl(String urlName) {
		Session session = this.sessionFactory.getCurrentSession();
		List<UrlShortner> listOfUrlShorten = session.createQuery("from UrlShortner url where url.originalUrl= :name").setParameter("name", urlName).list(); //to avoid sql injection
		if(!listOfUrlShorten.isEmpty() && listOfUrlShorten!=null){
			return Optional.ofNullable(listOfUrlShorten.get(0));
		}
		logger.info("UrlShortner loaded successfully, UrlShortner details="+listOfUrlShorten);
		return Optional.empty();

	}


}
