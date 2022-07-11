package br.com.auth.access;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <b>Token</b> carries out the <p>Client Name</p> and the <p>Token value</p> to be used as key to Access Auth application
 * others the uses it as a dependency
 *
 * @author lucascampos
 */
@JsonInclude( value = Include.NON_NULL )
public class Token implements Serializable {

	private static final long serialVersionUID = -4425166389381053728L;

	private String clientName;

	private String token;

	public Token( final String token, final String clientName ) {
		super();
		this.token = token;
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}

	public String getToken() {
		return token;
	}

	public void setClientName( String clientName ) {
		this.clientName = clientName;
	}

	public void setToken( String token ) {
		this.token = token;
	}

}
