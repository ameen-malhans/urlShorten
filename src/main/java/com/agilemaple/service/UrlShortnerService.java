package com.agilemaple.service;

import org.springframework.stereotype.Service;

@Service
public interface UrlShortnerService {

	 String shortenUrl(String longUrl);
	 String urlAnalytics(String shortUrl);
	 String findUrlById(String id);
	 void storeUrl(String id, String url);
	 Integer findCountById(String id);
	 void incrementCount(String id);
}
