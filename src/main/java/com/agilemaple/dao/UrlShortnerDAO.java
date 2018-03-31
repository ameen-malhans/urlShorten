package com.agilemaple.dao;

import org.springframework.stereotype.Service;

import com.agilemaple.model.UrlShortner;

import java.util.Optional;

@Service
public interface UrlShortnerDAO {

	Long saveUrl(UrlShortner urlId);
	Optional<UrlShortner> findeById(long id);
	Optional<UrlShortner> findByOriginalUrl(String urlName);
}
