package com.saltaku.api;

public class APIException extends Exception {

	public APIException() {
		super();
	}

	public APIException(String message, Throwable cause) {
		super(message, cause);
	}

	public APIException(String message) {
		super(message);
	}

	public APIException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4057444234580493284L;

}
