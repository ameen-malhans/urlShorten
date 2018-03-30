package com.agilemaple.service;

import org.springframework.stereotype.Service;

@Service
public interface UrlShortnerService {

	 String shortenUrl(String longUrl);
	 String urlAnalytics(String shortUrl);
}
