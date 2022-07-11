package br.com.auth.client.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.auth.enums.ClientOrigin;
import br.com.auth.enums.ClientRole;

/**
 * <b>Client</b> is json object representation of the Client
 *
 * @author Lucas Domáradzkí
 */
@JsonInclude(value = Include.NON_NULL)
public class Client implements Serializable {

	private static final long serialVersionUID = 3288636423966910827L;

	private List<String> authorities;

	private LocalDateTime birthday;

	private String cpf;

	private Integer id;

	private String login;

	private String name;

	private String nickname;

	private String oldPassword;

	private ClientOrigin origin;

	private String password;

	private ClientRole role;

	public Client() {
		super();
	}

	public Client( final String login, final String name ) {
		this.login = login;
		this.name = name;
	}

	public List<String> getAuthorities() {
		return this.authorities;
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

	public String getLogin() {
		return this.login;
	}

	public String getName() {
		return this.name;
	}

	public String getNickname() {
		return this.nickname;
	}

	public String getOldPassword() {
		return this.oldPassword;
	}

	public ClientOrigin getOrigin() {
		return this.origin;
	}

	public String getPassword() {
		return this.password;
	}

	public ClientRole getRole() {
		return this.role;
	}

	public void setAuthorities(final List<String> authorities) {
		this.authorities = authorities;
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

	public void setLogin(final String login) {
		this.login = login;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNickname(final String nickname) {
		this.nickname = nickname;
	}

	public void setOldPassword(final String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setOrigin(final ClientOrigin origin) {
		this.origin = origin;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setRole(final ClientRole role) {
		this.role = role;
	}

}
