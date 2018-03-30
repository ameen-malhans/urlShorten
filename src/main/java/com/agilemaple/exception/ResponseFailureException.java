package com.agilemaple.exception;

import com.ning.http.client.Response;


public class ResponseFailureException extends RuntimeException {
  
	public ResponseFailureException() {
  }

  public ResponseFailureException(String message) {
    super(message);
  }

  public ResponseFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResponseFailureException(Throwable cause) {
    super(cause);
  }

  public ResponseFailureException(String message, Response response) {
    super(message + " Status: " + response.getStatusCode() + " - " + response.getStatusText());
  }
}
