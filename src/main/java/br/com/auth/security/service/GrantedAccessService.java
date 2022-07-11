package br.com.auth.security.service;

import br.com.auth.access.GrantedAccess;
import br.com.auth.access.Token;
import br.com.auth.exception.AuthException;

/**
 * <b>GrantedAccessService</b>
 *
 * @author Lucas Domáradzkí
 */
public interface GrantedAccessService {

	/**
	 * Generates a new Token
	 *
	 * @param clientId Integer Client Id
	 * @return Token new created Token
	 */
	public Token generateToken( final Integer clientId );

	/**
	 * Log in the client
	 *
	 * @param clientLogin String Client Login (email)
	 * @return GrantedAccess
	 * @throws AuthException Error logging in client
	 */
	public GrantedAccess login( final String clientLogin ) throws AuthException;

	/**
	 * Log out the client
	 *
	 * @param token String token value
	 * @return boolean either the has been successfully logged out from the system
	 */
	public boolean logout( final String token );

	/**
	 * Validates the JWT Token sent from the request
	 *
	 * @param token String Token value
	 * @return GrantedAccess
	 * @throws AuthException
	 */
	public GrantedAccess validateToken( final String token ) throws AuthException;

}
