package br.com.auth.clienttoken.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.com.auth.client.entity.ClientEntity;

/**
 * <b>ClientTokenEntity</b> is the representation of the CLIENTTOKEN Database Table
 *
 * @author Lucas Domáradzkí
 */
@Entity
@Table( name = "AUTH_CLIENTTOKEN" )
public class ClientTokenEntity {

	@Column( name = "FGACTIVE" )
	private boolean active;

	@ManyToOne
	@JoinColumn( name = "CLIENTID" )
	private ClientEntity client;

	@Column( name = "DTEXPIRATION" )
	private LocalDateTime expirationDate;

	@Id
	@Column( name = "CLIENTTOKENID" )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Integer id;

	@Column( name = "DTISSUED" )
	private LocalDateTime issuedAt;

	@Column( name = "VLTOKEN" )
	private String token;

	@PrePersist
	private void beforePersist() {
		final LocalDateTime currentDate = LocalDateTime.now();

		this.issuedAt = currentDate;
	}

	public ClientEntity getClient() {
		return this.client;
	}

	public LocalDateTime getExpirationDate() {
		return this.expirationDate;
	}

	public Integer getId() {
		return this.id;
	}

	public LocalDateTime getIssuedAt() {
		return this.issuedAt;
	}

	public String getToken() {
		return this.token;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive( final boolean active ) {
		this.active = active;
	}

	public void setClient( final ClientEntity client ) {
		this.client = client;
	}

	public void setExpirationDate( final LocalDateTime expirationDate ) {
		this.expirationDate = expirationDate;
	}

	public void setId( final Integer id ) {
		this.id = id;
	}

	public void setIssuedAt( final LocalDateTime issueDate ) {
		this.issuedAt = issueDate;
	}

	public void setToken( final String token ) {
		this.token = token;
	}

}
