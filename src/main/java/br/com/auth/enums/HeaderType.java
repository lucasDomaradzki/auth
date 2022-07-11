package br.com.auth.enums;

/**
 * <b>HeaderTypes</b> is a list of enum values to used as keys for header names for this system
 *
 * @author Lucas Domáradzkí
 */
public enum HeaderType {

	CLIENTLOGIN( "clientlogin" ),

	CLIENTPASSWORD( "clientpassword" ),

	AUTHORIZATION( "Authorization" );

	private String headerName;

	private HeaderType( final String headerName ) {
		this.headerName = headerName;
	}

	public String getHeaderName() {
		return this.headerName;
	}

}
