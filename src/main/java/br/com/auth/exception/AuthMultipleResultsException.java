package br.com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.auth.response.AuthResponse;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AuthMultipleResultsException extends AuthException {

	private static final long serialVersionUID = 4696631601276238803L;

	private AuthResponse response;

	public AuthMultipleResultsException() {
		super();
	}

	public AuthMultipleResultsException(final String message) {
		super(message);
	}

	public AuthMultipleResultsException(final String message, final Throwable exception) {
		super(message, exception);
	}

	public AuthMultipleResultsException(final String message, final AuthResponse response ) {
		super(message);

		this.response = response;
	}

	public AuthMultipleResultsException( final AuthResponse response ) {
		super();
		this.response = response;
	}

	public AuthMultipleResultsException(final Throwable exception) {
		super(exception);
	}

	@Override
	public String getMessage() {
		if ( this.response != null ) {
			return this.response.getMessage();
		}

		return super.getMessage();
	}

	public Object getResponse() {
		return this.response;
	}

}
