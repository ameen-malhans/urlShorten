package com.agilemaple.service.implementation;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.agilemaple.dao.UrlAnalyticDAO;
import com.agilemaple.dao.UrlShortnerDAO;
import com.agilemaple.model.UrlAnalytic;
import com.agilemaple.model.UrlShortner;
import com.agilemaple.service.UrlShortnerService;
import com.agilemaple.utils.Base62;
import com.agilemaple.utils.RequestBuilder;

@Component
public class UrlShortenServiceImpl implements UrlShortnerService {

	@Autowired
	private UrlShortnerDAO urlShortnerDAO;
	
	@Autowired
	private UrlAnalyticDAO urlAnalyticDAO;

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

	@Override
	@Transactional
	public Long saveUrl(UrlShortner urlShortner) {
		long id = this.urlShortnerDAO.saveUrl(urlShortner);
		UrlAnalytic urlAnalytic =  new UrlAnalytic();
		urlAnalytic.setCount(0);
		urlAnalytic.setUrlShortner(urlShortner);
		urlAnalyticDAO.saveAnalytic(urlAnalytic);
		return id; 
	}

	@Override
	@Transactional
	public Optional<UrlShortner> findeById(long id) {
		return this.urlShortnerDAO.findeById(id);
	}

	@Override
	@Transactional
	public Optional<UrlShortner> findByOriginalUrl(String longUrl) {
		return this.urlShortnerDAO.findByOriginalUrl(longUrl);
	}

	@Override
	@Transactional
	public String shortenUrlByDb(String longUrl) {
		    UrlShortner urlShortner = null;
			// Quickly check the originalURL is already entered in our system.
			Optional<UrlShortner> exitURL = findByOriginalUrl(longUrl);

			if(exitURL.isPresent()) {
				// Retrieved from the system.
				urlShortner = exitURL.get();
			} else {
				urlShortner = new UrlShortner();
				// Otherwise, save a new original URL
				urlShortner.setOriginalUrl(longUrl);
				urlShortner.setCreatedOn(urlShortner.getCreatedOn());
				saveUrl(urlShortner);
			}
		return generateURLShorterner(urlShortner);
	}

	@Override
	@Transactional
	public String getUrlByDb(String shortUrl) {
			// Resolve a shortened URL to the initial ID.
			long id = Base62.toBase10(shortUrl);
			// Now find your database-record with the ID found
			Optional<UrlShortner> urlShortener = findeById(id);

			if(urlShortener.isPresent()) {
				String originalUrl = urlShortener.get().getOriginalUrl();
				UrlAnalytic urlAnalytic = urlShortener.get().getUrlAnalytic();
				urlAnalyticDAO.updateAnalytic(urlAnalytic);
				return originalUrl;
			}
		return null;
	}


	@Override
	@Transactional
	public Optional<UrlAnalytic> urlAnalyticsByDb(String shortUrl) {
		// Resolve a shortened URL to the initial ID.
		long id = Base62.toBase10(shortUrl);
		// Now find your database-record with the ID found
		Optional<UrlShortner> urlShortener = findeById(id);
		if(urlShortener.isPresent()) {
			return Optional.ofNullable(urlShortener.get().getUrlAnalytic());
		}
		
		return Optional.empty();
	}

	private String generateURLShorterner(UrlShortner urlShortner) {
		// Generate shortenedURL via base62 encode.
		String shortenedURL = Base62.toBase62(urlShortner.getId().intValue());
		return shortenedURL;
	}


}
