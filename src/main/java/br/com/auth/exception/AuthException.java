package br.com.auth.exception;

/**
 * <b>AuthException</b> generic exception thrown by the system
 * 
 * @author Lucas Domáradzkí
 */
public class AuthException extends Exception {

	private static final long serialVersionUID = 8763903207750468947L;

	public AuthException() {
		super();
	}

	public AuthException( final String message ) {
		super( message );
	}

	public AuthException( final String message, final Throwable exception ) {
		super( message, exception );
	}

	public AuthException( final Throwable exception ) {
		super( exception );
	}

}
