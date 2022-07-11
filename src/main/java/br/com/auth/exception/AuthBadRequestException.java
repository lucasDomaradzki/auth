package br.com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>AuthBadRequestException</b> exception thrown when HttpStatus Code 404 <b>NOT FOUND</b> might be sent to the response 
 * 
 * @author Lucas Domáradzkí
 */
@ResponseStatus( code = HttpStatus.BAD_REQUEST )
public class AuthBadRequestException extends AuthException {

	private static final long serialVersionUID = 1100655618119640436L;

	public AuthBadRequestException() {
		super();
	}

	public AuthBadRequestException( final String message ) {
		super( message );
	}

	public AuthBadRequestException( final String message, final Throwable exception ) {
		super( message, exception );
	}

	public AuthBadRequestException( final Throwable exception ) {
		super( exception );
	}

}
