package br.com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_FOUND )
public class AuthNotFoundException extends AuthException {

	private static final long serialVersionUID = -5483141401817671745L;

	public AuthNotFoundException() {
		super();
	}

	public AuthNotFoundException( final String message ) {
		super( message );
	}

	public AuthNotFoundException( final String message, final Throwable exception ) {
		super( message, exception );
	}

	public AuthNotFoundException( final Throwable exception ) {
		super( exception );
	}

}
