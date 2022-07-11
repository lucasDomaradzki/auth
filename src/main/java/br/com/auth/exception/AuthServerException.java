package br.com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>AuthBadRequestException</b> exception thrown when HttpStatus Code 500 <b>INTERNAL SERVER ERROR</b> might be sent to the response 
 * 
 * @author Lucas Domáradzkí
 */
@ResponseStatus( code = HttpStatus.INTERNAL_SERVER_ERROR )
public class AuthServerException extends AuthException {

	private static final long serialVersionUID = 8704362753684527986L;

	public AuthServerException() {
		super();
	}

	public AuthServerException( final String message ) {
		super( message );
	}

	public AuthServerException( final String message, final Throwable exception ) {
		super( message, exception );
	}

	public AuthServerException( final Throwable exception ) {
		super( exception );
	}

}
