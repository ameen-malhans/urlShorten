package com.agilemaple.service.implementation;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.agilemaple.service.UrlShortnerService;
import com.agilemaple.service.utils.RequestBuilder;

@Component
public class UrlShortenServiceImpl implements UrlShortnerService {

	private Map<String, String> urlByIdMap = new ConcurrentHashMap<>();
	private Map<String, Integer> countByIdMap = new ConcurrentHashMap<>();
	
	RequestBuilder requestBuilder = new RequestBuilder();

	private static final Logger log = LoggerFactory.getLogger(UrlShortenServiceImpl.class);

	@Override
	public String shortenUrl(String longUrl) {
		try {
			RequestBuilder builder = requestBuilder.shorten(longUrl);
			return builder.execute();
		} catch (IOException | ExecutionException | InterruptedException e) {
			log.error("Exception UrlShortenServiceImpl::shortenUrl", e);
		}
		return null;

	}

	@Override
	public String urlAnalytics(String shortUrl) {
		try {
			RequestBuilder builder = requestBuilder.expand(shortUrl);
			return builder.execute();
		} catch (IOException | ExecutionException | InterruptedException e) {
			log.error("Exception UrlShortenServiceImpl::shortenUrl", e);
		}
		return null;
	}

	@Override
	public String findUrlById(String id) {
		return urlByIdMap.get(id);
	}
	
	@Override
	public Integer findCountById(String id) {
		return countByIdMap.get(id);
	}
	
	@Override
	public void incrementCount(String id) {
		Integer count = countByIdMap.get(id);
		countByIdMap.put(id, ++count);
	}

	@Override
	public void storeUrl(String id, String url) {
		 urlByIdMap.put(id, url);
		 countByIdMap.put(id, 0);
	}

}
