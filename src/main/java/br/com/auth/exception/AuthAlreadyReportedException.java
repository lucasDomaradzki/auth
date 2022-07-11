package br.com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.ALREADY_REPORTED )
public class AuthAlreadyReportedException extends AuthException {

	private static final long serialVersionUID = -691891016748830481L;

	public AuthAlreadyReportedException() {
		super();
	}

	public AuthAlreadyReportedException( final String message ) {
		super( message );
	}

	public AuthAlreadyReportedException( final String message, final Throwable exception ) {
		super( message, exception );
	}

	public AuthAlreadyReportedException( final Throwable exception ) {
		super( exception );
	}

}
