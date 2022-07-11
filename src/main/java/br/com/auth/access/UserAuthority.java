package br.com.auth.access;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <b>UserAuthority</b> is a String value defined in DataBase structure that represents a certain type of authority the client may have in the system.
 * Some clients might have a list of these authorities
 *
 * @author Lucas Domáradzkí
 */
@JsonInclude( value = Include.NON_NULL )
public class UserAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 9195429044032546356L;

	private String authority;

	public UserAuthority( String authority ) {
		super();
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority( String authority ) {
		this.authority = authority;
	}

}
