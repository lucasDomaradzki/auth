package br.com.auth.client.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import br.com.auth.enums.ClientOrigin;
import br.com.auth.enums.ClientRole;

/**
 * <b>ClientEntity</b> is the representation of the CLIENT Database Table
 *
 * @author Lucas Domáradzkí
 */
@Entity
@Table(name = "AUTH_CLIENT")
public class ClientEntity {

	@Column(name = "FGACTIVE")
	private boolean active;

	@Column(name = "DTBIRTHDAY")
	private LocalDateTime birthday;

	@Column(name = "CPF")
	private String cpf;

	@Id
	@Column(name = "CLIENTID")
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Integer id;

	@Column(name = "DTISSUED")
	private LocalDateTime issuedAt;

	@Column(name = "EMAIL")
	private String login;

	@Column(name = "NAME")
	private String name;

	@Column(name = "NICKNAME")
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGIN")
	private ClientOrigin origin;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "PASSWORDEXPIRATION")
	private LocalDateTime passwordExpiration;

	@Column(name = "ROLE")
	@Enumerated(EnumType.STRING)
	private ClientRole role;

	@Column(name = "DTUPDATE")
	private LocalDateTime updateDate;

	@PrePersist
	private void beforePersist() {
		this.issuedAt = LocalDateTime.now();
		this.updateDate = LocalDateTime.now();
	}

	@PreUpdate
	private void beforeUpdate() {
		this.updateDate = LocalDateTime.now();
	}

	public LocalDateTime getBirthday() {
		return this.birthday;
	}

	public String getCpf() {
		return this.cpf;
	}

	public Integer getId() {
		return this.id;
	}

	public LocalDateTime getIssuedAt() {
		return this.issuedAt;
	}

	public String getLogin() {
		return this.login;
	}

	public String getName() {
		return this.name;
	}

	public String getNickname() {
		return this.nickname;
	}

	public ClientOrigin getOrigin() {
		return this.origin;
	}

	public String getPassword() {
		return this.password;
	}

	public LocalDateTime getPasswordExpiration() {
		return this.passwordExpiration;
	}

	public ClientRole getRole() {
		return this.role;
	}

	public LocalDateTime getUpdateDate() {
		return this.updateDate;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public void setBirthday(final LocalDateTime birthday) {
		this.birthday = birthday;
	}

	public void setCpf(final String cpf) {
		this.cpf = cpf;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setIssuedAt(final LocalDateTime issueDate) {
		this.issuedAt = issueDate;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNickname(final String nickname) {
		this.nickname = nickname;
	}

	public void setOrigin(final ClientOrigin origin) {
		this.origin = origin;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setPasswordExpiration(final LocalDateTime passwordExpiration) {
		this.passwordExpiration = passwordExpiration;
	}

	public void setRole(final ClientRole role) {
		this.role = role;
	}

	public void setUpdateDate(final LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

}
