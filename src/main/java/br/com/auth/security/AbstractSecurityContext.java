package br.com.auth.security;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.auth.access.GrantedAccess;

/**
 * <b>AbstractSecurityContext</b> Abstracts Security Context from SpringBoot Security to access Authentication data
 * 
 * @author Lucas Domáradzkí
 */
public abstract class AbstractSecurityContext {

	/**
	 * Return the Client GrantedAccess
	 * 
	 * @return GrantedAccess
	 */
	public GrantedAccess getGrantedAccess() {
		return (GrantedAccess) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
