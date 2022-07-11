package br.com.auth.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.auth.access.GrantedAccess;
import br.com.auth.access.Token;
import br.com.auth.exception.AuthException;
import br.com.auth.security.repository.GrantedAccessRepository;

/**
 * <b>GrantedAccessServiceImpl</b>
 *
 * @author Lucas Domáradzkí
 */
@Service
public class GrantedAccessServiceImpl implements GrantedAccessService {

	@Autowired
	private GrantedAccessRepository repository;

	@Override
	public Token generateToken( final Integer clientId ) {
		return this.repository.generateToken( clientId );
	}

	@Override
	public GrantedAccess login( final String clientLogin ) throws AuthException {
		return this.repository.login( clientLogin );
	}

	@Override
	public boolean logout( final String token ) {
		return this.repository.logout( token );
	}

	@Override
	public GrantedAccess validateToken( final String token ) throws AuthException {
		return this.repository.validateToken( token );
	}

}
