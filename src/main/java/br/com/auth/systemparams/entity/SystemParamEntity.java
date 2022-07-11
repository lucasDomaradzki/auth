package br.com.auth.systemparams.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <b>SystemParamEntity</b> is the representation of the SYSTEMPARAM Database Table
 *
 * @author Lucas Domáradzkí
 */
@Entity
@Table( name = "AUTH_SYSTEMPARAM" )
public class SystemParamEntity {

	@Id
	@Column( name = "SYSTEMPARAMID" )
	private String id;

	@Column( name = "NAME" )
	private String name;

	@Column( name = "PARAM" )
	private String param;

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getParam() {
		return this.param;
	}

	public void setId( final String id ) {
		this.id = id;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public void setParam( final String param ) {
		this.param = param;
	}

}
