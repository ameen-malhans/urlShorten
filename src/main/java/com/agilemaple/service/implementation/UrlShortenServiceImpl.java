package com.agilemaple.service.implementation;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.agilemaple.service.UrlShortnerService;
import com.agilemaple.service.utils.RequestBuilder;

public class UrlShortenServiceImpl implements UrlShortnerService {

	@Autowired
	RequestBuilder requestBuilder;

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

}
