package br.com.auth.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.auth.exception.AuthException;

/**
 * Response object returned when an exception is caught by the application
 *
 * @author lucas.domaradzki
 */
@JsonInclude( value = Include.NON_NULL )
public class AuthResponse implements Serializable {

	private static final long serialVersionUID = -4792450841847343995L;

	private String message;

	private String path;

	private HttpStatus status;

	private LocalDateTime timestamp;

	public AuthResponse() {
		super();
	}

	public AuthResponse( final String message ) {
		this.message = message;
	}

	public AuthResponse( final HttpStatus status, final HttpServletRequest request, final AuthenticationException failed ) {
		this.message = StringUtils.equals( failed.getMessage().toLowerCase(), "bad credentials" ) ? "Login ou senha incorretos." : failed.getMessage();
		this.path = request.getRequestURI();
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}

	public AuthResponse( final HttpStatus status, final HttpServletRequest request, final AuthException failed ) {
		this.message = failed.getMessage();
		this.path = request.getRequestURI();
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}

	public AuthResponse( final HttpStatus status, final HttpServletRequest request, final String message ) {
		this.message = message;
		this.path = request.getRequestURI();
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}

	public String getMessage() {
		return this.message;
	}

	public String getPath() {
		return this.path;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public void setMessage( final String message ) {
		this.message = message;
	}

	public void setPath( final String path ) {
		this.path = path;
	}

	public void setStatus( final HttpStatus status ) {
		this.status = status;
	}

	public void setTimestamp( final LocalDateTime timestamp ) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		final Map<String, Object> authResponse = new HashMap<>();
		authResponse.put( "timestamp", this.getTimestamp().toString() );
		authResponse.put( "path", this.getPath() );
		authResponse.put( "message", this.getMessage() );
		authResponse.put( "status", this.getStatus().value() );

		try {
			return new ObjectMapper().writeValueAsString( authResponse );
		} catch ( final JsonProcessingException e ) {
			return this.getMessage();
		}
	}

}
