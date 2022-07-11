package br.com.auth.access;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.auth.enums.ClientRole;

/**
 * <b>GrantedAcess</b> represent the whole Access the client shall have in the system, including his/her {@link br.com.auth.access.UserAuthority}
 *
 * @author Lucas Domáradzkí
 */
public class GrantedAccess implements UserDetails {

	private static final long serialVersionUID = -8998871132955036638L;

	private LocalDateTime accountExpirationDate;

	private boolean active;

	private LocalDateTime credentialExpirationDate;

	private String email;

	private Integer idUsuario;

	private String password;

	private ClientRole role;

	private String token;

	private List<UserAuthority> userAuthorities;

	private String userName;

	public LocalDateTime getAccountExpirationDate() {
		return this.accountExpirationDate;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.userAuthorities;
	}

	public LocalDateTime getCredentialExpirationDate() {
		return this.credentialExpirationDate;
	}

	public String getEmail() {
		return this.email;
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public ClientRole getRole() {
		return this.role;
	}

	public String getToken() {
		return this.token;
	}

	public List<UserAuthority> getUserAuthorities() {
		return this.userAuthorities;
	}

	@Override
	public String getUsername() {
		return this.getUserName();
	}

	public String getUserName() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		final LocalDateTime currentDate = LocalDateTime.now();

		return !this.getAccountExpirationDate().isBefore( currentDate );
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isActive();
	}

	public boolean isActive() {
		return this.active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		final LocalDateTime currentDate = LocalDateTime.now();

		return !this.getCredentialExpirationDate().isBefore( currentDate );
	}

	@Override
	public boolean isEnabled() {
		return this.isActive();
	}

	public void setAccountExpirationDate( final LocalDateTime accountExpirationDate ) {
		this.accountExpirationDate = accountExpirationDate;
	}

	public void setActive( final boolean active ) {
		this.active = active;
	}

	public void setCredentialExpirationDate( final LocalDateTime credentialExpirationDate ) {
		this.credentialExpirationDate = credentialExpirationDate;
	}

	public void setEmail( final String email ) {
		this.email = email;
	}

	public void setIdUsuario( final Integer idUsuario ) {
		this.idUsuario = idUsuario;
	}

	public void setPassword( final String password ) {
		this.password = password;
	}

	public void setRole( final ClientRole role ) {
		this.role = role;
	}

	public void setToken( final String token ) {
		this.token = token;
	}

	public void setUserAuthorities( final List<UserAuthority> userAuthorities ) {
		this.userAuthorities = userAuthorities;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

}
