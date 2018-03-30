package com.agilemaple.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agilemaple.dto.ShortenUrlRequest;
import com.agilemaple.exception.ResponseFailureException;
import com.agilemaple.service.UrlShortnerService;
import com.agilemaple.service.utils.RequestBuilder;

import pl.project13.jgoogl.exceptions.InvalidGooGlUrlException;

@Controller
@RequestMapping("/api") 
public class UrlShortnerController {

	@Autowired
	UrlShortnerService urlShortnerService;
	
	@RequestMapping(value="shortenurl/", method = RequestMethod.POST)
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
	
	@RequestMapping(value="analytics/", method = RequestMethod.POST)
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
	
}
