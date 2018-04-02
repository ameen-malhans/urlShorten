package com.agilemaple.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agilemaple.dto.Analytics;
import com.agilemaple.dto.ShortenUrlRequest;
import com.agilemaple.dto.ShortenUrlResponse;
import com.agilemaple.exception.ResponseFailureException;
import com.agilemaple.model.UrlAnalytic;
import com.agilemaple.service.UrlShortnerService;
import com.agilemaple.utils.RequestBuilder;
import com.google.common.hash.Hashing;

import pl.project13.jgoogl.exceptions.InvalidGooGlUrlException;

@Controller
@RequestMapping("/api") 
public class UrlShortnerController {

	@Autowired
	UrlShortnerService urlShortnerService;
	
	@RequestMapping(value="google/shortenurl", method = RequestMethod.POST)
	public @ResponseBody String shortenUrl(HttpServletRequest httpRequest,
            @Valid @RequestBody ShortenUrlRequest request) {
		String longUrl = request.getUrl();
		if(longUrl==null /*|| RequestBuilder.throwIfNotGooGlUrl(longUrl)*/) {
			throw new InvalidGooGlUrlException("It seems that the url: '" + longUrl + "' is invalid...");
		}
		String shortUrl = urlShortnerService.shortenUrl(longUrl);
		if(shortUrl==null) {
			 throw new ResponseFailureException("Something went wrong with google api and response is null" + shortUrl);
		}
		return shortUrl;
	}
	
	@RequestMapping(value="google/analytics", method = RequestMethod.POST)
	public @ResponseBody String urlAnalytics(HttpServletRequest httpRequest,
            @Valid @RequestBody ShortenUrlRequest request) {
		String shortUrl = request.getUrl();
		if(shortUrl==null || RequestBuilder.throwIfNotGooGlUrl(shortUrl)) {
			throw new InvalidGooGlUrlException("It seems that the url: '" + shortUrl + "' is invalid...");
		}
		String analytics = urlShortnerService.urlAnalytics(shortUrl);
		if(analytics==null) {
			 throw new ResponseFailureException("Something went wrong with google api and response is null" + shortUrl);
		}
		return analytics;
	}
	
   
	
	@RequestMapping(value = "inmemory/shortenurl", method = RequestMethod.POST)
	public @ResponseBody ShortenUrlResponse shortenUrlInBuild(HttpServletRequest httpRequest, @Valid @RequestBody ShortenUrlRequest request) {
		String url = request.getUrl();
		if (!isUrlValid(url)) {
			throw new InvalidGooGlUrlException("It seems that the url: '" + url + "' is invalid...");
		}
		final String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
		urlShortnerService.storeUrl(id, url);
		String requestUrl = httpRequest.getRequestURL().toString();
		String prefix = requestUrl.substring(0, requestUrl.indexOf(httpRequest.getRequestURI(), "http://".length()));
		ShortenUrlResponse response = new ShortenUrlResponse();
		response.setUrl(prefix + "/api/" + id);
		return response;
	}
	
	@RequestMapping(value = "inmemory/analytics", method = RequestMethod.POST)
	public @ResponseBody Analytics analyticsInBuild(HttpServletRequest httpRequest, @Valid @RequestBody ShortenUrlRequest request) {
		Analytics analytics = new Analytics();
		String shortUrl = request.getUrl();
		if(shortUrl!=null) {
			String id =  shortUrl.substring(shortUrl.lastIndexOf("/")+1,shortUrl.length());
			if(id!=null) {
				Integer count = urlShortnerService.findCountById(id);	
				analytics.setCount(count);
			}
			
		}
		return analytics;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void redirectToUrl(@PathVariable String id, HttpServletResponse resp) throws Exception {
		final String url = urlShortnerService.findUrlById(id);
		Integer count = urlShortnerService.findCountById(id);
		if (url != null && count!=null) {
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
			resp.setDateHeader("Expires", 0); // Proxies.
			resp.addHeader("Location", url);
			urlShortnerService.incrementCount(id);
			resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
    
    private boolean isUrlValid(String url) {
        boolean valid = true;
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            valid = false;
        }
        return valid;
    }

	@RequestMapping(value = "db/shortenurl", method = RequestMethod.POST)
	public @ResponseBody ShortenUrlResponse getshortenUrlByDb(HttpServletRequest httpRequest, @Valid @RequestBody ShortenUrlRequest request) {
		String url = request.getUrl();
		if (!isUrlValid(url)) {
			throw new InvalidGooGlUrlException("It seems that the url: '" + url + "' is invalid...");
		}
		String shortUrl = urlShortnerService.shortenUrlByDb(url);
		String requestUrl = httpRequest.getRequestURL().toString();
		String prefix = requestUrl.substring(0, requestUrl.indexOf(httpRequest.getRequestURI(), "http://".length()));
		ShortenUrlResponse response = new ShortenUrlResponse();
		response.setUrl(prefix + "/api/db/" + shortUrl);
		return response;
	}
	
	@RequestMapping(value = "db/analytics", method = RequestMethod.POST)
	public @ResponseBody Analytics analyticsByDb(HttpServletRequest httpRequest, @Valid @RequestBody ShortenUrlRequest request) {
		Analytics analytics = new Analytics();
		String shortUrl = request.getUrl();
		if(shortUrl!=null) {
			String suffixId =  shortUrl.substring(shortUrl.lastIndexOf("/")+1,shortUrl.length());
			if(suffixId!=null) {
				Optional<UrlAnalytic> analyticsFromDb = urlShortnerService.urlAnalyticsByDb(suffixId);
				analytics.setCount(analyticsFromDb.get().getCount());
			}
			
		}
		return analytics;
	}

	@RequestMapping(value = "db/{id}", method = RequestMethod.GET)
	public void redirectToUrlByDb(@PathVariable String id, HttpServletResponse resp) throws Exception {
		String longUrl = urlShortnerService.getUrlByDb(id);
		if (longUrl != null) {
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
			resp.setDateHeader("Expires", 0); // Proxies.
			resp.addHeader("Location", longUrl);
			resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	
}
