package com.agilemaple.configuration;

public enum Version {
  /**
   * First and currently only version of GooGl
   */
  V1("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyBup6nZuuKUQIbXv-W5VxBE0FXpnz58xSY");

  public final String serviceUrl;

  Version(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }
}
