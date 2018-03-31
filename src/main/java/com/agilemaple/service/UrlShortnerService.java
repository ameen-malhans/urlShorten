package com.agilemaple.service;

import org.springframework.stereotype.Service;

import com.agilemaple.model.UrlShortner;

import java.util.Optional;

@Service
public interface UrlShortnerService {

	 String shortenUrl(String longUrl);
	 String urlAnalytics(String shortUrl);
	 String findUrlById(String id);
	 void storeUrl(String id, String url);
	 Integer findCountById(String id);
	 void incrementCount(String id);
	 Long saveUrl(UrlShortner urlId);
	 Optional<UrlShortner> findeById(long id);
	 Optional<UrlShortner> findByOriginalUrl(String urlName);
	 String shortenUrlByDb(String longUrl);
	 String getUrlByDb(String shortUrl);
	 String urlAnalyticsByDb(String shortUrl);

}
