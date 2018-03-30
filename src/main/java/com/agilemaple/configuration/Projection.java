package com.agilemaple.configuration;


public enum Projection {

  /**
   * Use this projection if you want click statistics
   */
  ANALYTICS_CLICKS("ANALYTICS_CLICKS");

  public final String urlParamValue;

  Projection(String urlParamValue) {
    this.urlParamValue = urlParamValue;
  }
}
