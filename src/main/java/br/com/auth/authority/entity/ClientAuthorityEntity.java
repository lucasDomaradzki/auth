package br.com.auth.authority.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.auth.client.entity.ClientEntity;

/**
 * <b>ClientAuthorityEntity</b> is the representation of the CLIENTAUTHORITY Database Table
 *
 * @author Lucas Domáradzkí
 */
@Entity
@Table( name = "AUTH_CLIENTAUTHORITY" )
public class ClientAuthorityEntity implements Serializable {

	private static final long serialVersionUID = -5499532998749750830L;

	@Column( name = "FGACTIVE" )
	private boolean active;

	@OneToOne
	@JoinColumn( name = "AUTHORITYID" )
	private AuthorityEntity authority;

	@OneToOne
	@JoinColumn( name = "CLIENTID" )
	private ClientEntity client;

	@Id
	@Column( name = "CLIENTAUTHORITYID" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Integer id;

	public AuthorityEntity getAuthority() {
		return this.authority;
	}

	public ClientEntity getClient() {
		return this.client;
	}

	public Integer getId() {
		return this.id;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive( final boolean active ) {
		this.active = active;
	}

	public void setAuthority( final AuthorityEntity authority ) {
		this.authority = authority;
	}

	public void setClient( final ClientEntity client ) {
		this.client = client;
	}

	public void setId( final Integer id ) {
		this.id = id;
	}

}
