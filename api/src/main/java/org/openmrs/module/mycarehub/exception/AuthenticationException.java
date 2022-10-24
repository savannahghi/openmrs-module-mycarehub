package org.openmrs.module.mycarehub.exception;

/**
 * Wraps errors thrown during authentication with myCareHub
 */
public class AuthenticationException extends Throwable {
	
	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(String message) {
		super(message);
	}
	
	public AuthenticationException(Throwable throwable) {
		super(throwable);
	}
}
