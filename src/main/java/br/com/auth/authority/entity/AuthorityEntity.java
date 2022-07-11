package br.com.auth.authority.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <b>AuthorityEntity</b> is the representation of the AUTHORITY Database Table
 *
 * @author Lucas Domáradzkí
 */
@Entity
@Table( name = "AUTH_AUTHORITY" )
public class AuthorityEntity implements Serializable {

	private static final long serialVersionUID = 5808144123497510037L;

	@Column( name = "FGACTIVE" )
	private boolean active;

	@Id
	@Column( name = "AUTHORITYID" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Integer id;

	@Column( name = "SGAUTHORITY" )
	private String name;

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive( final boolean active ) {
		this.active = active;
	}

	public void setId( final Integer id ) {
		this.id = id;
	}

	public void setName( final String name ) {
		this.name = name;
	}

}
