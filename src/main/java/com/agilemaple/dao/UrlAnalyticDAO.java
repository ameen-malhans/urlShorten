package com.agilemaple.dao;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agilemaple.model.UrlAnalytic;

@Service
public interface UrlAnalyticDAO {

	Long saveAnalytic(UrlAnalytic urlAnalytic);
	void updateAnalytic(UrlAnalytic urlAnalytic);
	Optional<UrlAnalytic> findAnalyticById(long id);
}
