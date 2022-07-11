package br.com.auth.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.auth.exception.AuthException;

/**
 * <b>AuthService</b> implements {@link org.springframework.security.core.userdetails.UserDetailsService} and| is responsible 
 * load Client Info and send {@link org.springframework.security.core.userdetails.UserDetails} to Spring Security for 
 * authentication purposes
 * 
 * @author Lucas Domáradzkí
 */
public final class AuthService implements UserDetailsService {

	private final GrantedAccessService service;

	public AuthService( final GrantedAccessService acessControl ) {
		super();
		this.service = acessControl;
	}

	@Override
	public UserDetails loadUserByUsername( final String clienLogin ) throws UsernameNotFoundException {
		try {
			return this.service.login( clienLogin );
		} catch ( final AuthException e ) {
			throw new UsernameNotFoundException( e.getMessage(), e );
		}
	}

}
