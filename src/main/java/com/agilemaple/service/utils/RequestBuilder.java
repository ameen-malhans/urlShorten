package com.agilemaple.service.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilemaple.configuration.Projection;
import com.agilemaple.configuration.Version;
import com.agilemaple.exception.ResponseFailureException;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

import pl.project13.jgoogl.exceptions.InvalidGooGlUrlException;
import pl.project13.jgoogl.gson.GooGlStatusDeserializer;
import pl.project13.jgoogl.gson.ISO8601DateDeserializer;
import pl.project13.jgoogl.response.v1.enums.GooGlStatus;

import com.ning.http.client.Response;


public class RequestBuilder {

	private static final Logger log = LoggerFactory.getLogger(RequestBuilder.class);

  private AsyncHttpClient asyncHttpClient;

  private final String APP_JSON   = "application/json";
  private final String API_KEY    = "key"; 
  private final String SHORT_URL  = "shortUrl";
  private final String LONG_URL   = "longUrl";
  private final String PROJECTION = "projection";

  private Version        version    = Version.V1;
  private Projection     projection = Projection.ANALYTICS_CLICKS;
  private Map<String, String> params     = com.google.common.collect.Maps.newHashMap();
  private Gson gson;

  public RequestBuilder() {
    this.asyncHttpClient = new AsyncHttpClient();
    this.gson = RequestBuilder.get();
  }

  public RequestBuilder apiKey(String apiKey) {
    if (!hasText(apiKey)) {
      params.remove(API_KEY);
    } else {
      params.put(API_KEY, apiKey);
    }
    return this;
  }

  public RequestBuilder shorten(String longUrl) {
    params.remove(SHORT_URL);
    params.put(LONG_URL, longUrl);
    return this;
  }

  public RequestBuilder expand(String shortUrl) {
    params.remove(LONG_URL);
    params.put(SHORT_URL, urlEncode(shortUrl));

    return this;
  }

  public RequestBuilder projection(Projection projection) {
    this.projection = projection;
    return this;
  }

  public RequestBuilder apiVersion(Version apiVersion) {
    this.version = apiVersion;
    return this;
  }

  public String execute() throws IOException, ExecutionException, InterruptedException {
    BoundRequestBuilder builder = prepareBuilder();

    Future<Response> futureResponse = builder.execute();
    Response response = futureResponse.get();

    String responseBody = response.getResponseBody();
    if (!hasText(responseBody)) {
      throw new ResponseFailureException("Got invalid response.", response);
    }
    log.info("GOT: " + responseBody);

    return responseBody;
  }

  private BoundRequestBuilder prepareBuilder() {
    BoundRequestBuilder builder;

    if (shouldUsePost()) {
      String url = version.serviceUrl;
      String data = gson.toJson(params);
      log.info("POST: " + url + " with data: " + data);

      builder = asyncHttpClient.preparePost(url)
                               .setBody(data);
    } else {
      String url = prepareGetUrl(version.serviceUrl);
      log.info("GET: " + url + " with params: " + PROJECTION + " = " + projection.urlParamValue);
      builder = asyncHttpClient.prepareGet(url);
    }

    builder.addHeader("Content-type", APP_JSON)
           .addHeader("Accept", APP_JSON);

    return builder;
  }

  private String prepareGetUrl(String serviceUrl) {
    params.put(PROJECTION, projection.urlParamValue);

    String queryUrl = serviceUrl + "&" + Joiner.on("&").withKeyValueSeparator("=").join(params);

    params = com.google.common.collect.Maps.newHashMap(); // reset params

    return queryUrl;
  }


  private String urlEncode(String value) {
    String encode = value;
    try {
      encode = URLEncoder.encode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.error("Could not urlEncode parameter", e);
    }
    return encode;
  }

  private boolean shouldUsePost() {
    return params.containsKey(LONG_URL); //todo not clean intention, make this better code
  }
  
  public static boolean hasText(String value) {
	    return value != null && value.length() > 0;
	  }
  
  public static Gson get() {
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.setPrettyPrinting();

	    gsonBuilder.registerTypeAdapter(GooGlStatus.class, new GooGlStatusDeserializer());
	    gsonBuilder.registerTypeAdapter(Date.class, new ISO8601DateDeserializer());

	    return gsonBuilder.create();
	  }
  
  public static boolean throwIfNotGooGlUrl(String url) {
	    if (!(url.startsWith("goo.gl/") || url.startsWith("https://www.goo.gl/") || url.startsWith("https://goo.gl/"))) {
	      return Boolean.TRUE;
	    }
	    return Boolean.FALSE;
	  }
}
