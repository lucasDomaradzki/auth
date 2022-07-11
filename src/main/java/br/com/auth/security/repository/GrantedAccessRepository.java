package br.com.auth.security.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.auth.access.GrantedAccess;
import br.com.auth.access.Token;
import br.com.auth.access.UserAuthority;
import br.com.auth.authority.entity.AuthorityEntity;
import br.com.auth.client.entity.ClientEntity;
import br.com.auth.client.repository.ClientRepository;
import br.com.auth.clienttoken.entity.ClientTokenEntity;
import br.com.auth.exception.AuthBadRequestException;
import br.com.auth.exception.AuthException;
import br.com.auth.exception.AuthServerException;
import br.com.auth.systemparams.repository.SystemParamRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * <b>GrantedAccessRepository</b> performs peristence and search of Client and ClientToken
 *
 * @author Lucas Domáradzkí
 */
@Repository
public class GrantedAccessRepository {

	private static final String SERVER_SECRET = "server.secret";

	private static final String TOKEN_EXPIRATION = "token.expiration";

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SystemParamRepository systemRepository;

	/**
	 * Creates a new Token by searching the Client by his/her id
	 *
	 * @param clientId Integer Client Id
	 * @return Token newly created Jwt Token
	 */
	private Token createNewToken( final Integer clientId ) {
		final ClientEntity client = this.findClientByClientId( clientId );

		final LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes( this.systemRepository.findNumericParam( TOKEN_EXPIRATION ) );
		final JwtBuilder builder = Jwts.builder().setSubject( client.getLogin() );
		builder.setExpiration( Date.from( expirationDateTime.atZone( ZoneId.systemDefault() ).toInstant() ) );
		builder.setHeaderParam( "date", LocalDateTime.now().format( DateTimeFormatter.ISO_LOCAL_DATE_TIME ) );
		builder.setHeaderParam( "email", client.getLogin() );
		builder.setIssuedAt( new Date() );
		builder.setSubject( client.getLogin() );
		builder.signWith( SignatureAlgorithm.HS512, this.systemRepository.findByteParam( SERVER_SECRET ) );

		final String token = builder.compact();

		this.persistClientToken( client, token, expirationDateTime );

		return new Token( token, client.getName() );
	}

	/**
	 * Disables the previous expired token
	 *
	 * @param clientToken ClientTokenEntity Token to be disabled
	 */
	private void disablePreviousToken( final ClientTokenEntity clientToken ) {
		if ( clientToken == null ) {
			return;
		}

		clientToken.setActive( false );
		this.entityManager.persist( clientToken );
	}

	/**
	 * Find Client Authorities
	 *
	 * @param client ClientEntity
	 * @return List<UserAuthority>
	 */
	private List<UserAuthority> findClientAuthorities( final ClientEntity client ) {
		final String jpql = "SELECT client.authority FROM ClientAuthorityEntity client WHERE client.client.id = :clientId AND client.active = :active";
		final TypedQuery<AuthorityEntity> query = this.entityManager.createQuery( jpql, AuthorityEntity.class );
		query.setParameter( "clientId", client.getId() );
		query.setParameter( "active", true );

		final List<AuthorityEntity> clientAuthorities = query.getResultList();

		if ( clientAuthorities.isEmpty() ) {
			return Collections.emptyList();
		}

		final List<UserAuthority> grantedAuthorities = new ArrayList<>();

		for ( final AuthorityEntity authority : clientAuthorities ) {
			grantedAuthorities.add( new UserAuthority( authority.getName() ) );
		}

		return grantedAuthorities;
	}

	/**
	 * Returns the ClientEntity by its id number
	 *
	 * @param clientId Integer Client Id
	 * @return ClientEntity
	 */
	private ClientEntity findClientByClientId( final Integer clientId ) {
		return this.entityManager.find( ClientEntity.class, clientId );
	}

	/**
	 * Returns the ClientTokenEntity searching by the Client Id
	 *
	 * @param clientId Integer Client Id
	 * @return ClientTokenEntity
	 */
	private ClientTokenEntity findClientTokenByClientId( final Integer clientId ) {
		try {
			final String jpql = "FROM ClientTokenEntity WHERE client.id = :clientId AND active = :active";
			final TypedQuery<ClientTokenEntity> query = this.entityManager.createQuery( jpql, ClientTokenEntity.class );
			query.setParameter( "clientId", clientId );
			query.setParameter( "active", true );

			return query.getSingleResult();
		} catch ( final NoResultException e ) {
			return null;
		}
	}

	@Transactional( rollbackFor = AuthException.class )
	public Token generateToken( final Integer clientId ) {
		final ClientTokenEntity clientToken = this.findClientTokenByClientId( clientId );

		if ( this.isTokenStillValid( clientToken ) ) {
			return new Token( clientToken.getToken(), clientToken.getClient().getName() );
		}

		this.disablePreviousToken( clientToken );

		return this.createNewToken( clientId );
	}

	/**
	 * Return the Subject from the token value by parsing the JWS claims
	 *
	 * @param token String Token from the request
	 * @return String Client login (Subject)
	 * @throws AuthException Error when parsing and fetching the client from token
	 */
	private String getSubject( final String token ) throws AuthException {
		try {
			final Claims body = Jwts.parser().setSigningKey( this.systemRepository.findByteParam( SERVER_SECRET ) ).parseClaimsJws( token ).getBody();
			if ( body == null || body.getSubject() == null ) {
				return null;
			}

			return body.getSubject();
		} catch ( final ExpiredJwtException e ) {
			throw new AuthServerException( "O Token informado está expirado", e );
		} catch ( final UnsupportedJwtException e ) {
			throw new AuthServerException( "Erro desconhecido ao verificar o Token", e );
		} catch ( final MalformedJwtException e ) {
			throw new AuthServerException( "O Token informado é inválido", e );
		} catch ( final SignatureException e ) {
			throw new AuthServerException( "O Token informado é inválido", e );
		}
	}

	/**
	 * Match the clientLogin, retrieved from token in the request and clientLogin stored in Database
	 *
	 * @param clientLogin String Client Login retrieved from token
	 * @param client ClientEntity Client Entity from Database
	 * @return boolean true -> are identical. false -> they do not match
	 */
	private boolean isTokenLoginEqualToClientLogin( final String clientLogin, final ClientEntity client ) {
		return client.getLogin().equals( clientLogin );
	}

	/**
	 * Return if the token is expiration is still valid
	 *
	 * @param clientToken ClientTokenEntity
	 * @return boolean true -> token is still valid. false -> token is no longer valid
	 */
	private boolean isTokenStillValid( final ClientTokenEntity clientToken ) {
		return clientToken != null && clientToken.getExpirationDate().isAfter( LocalDateTime.now() );
	}

	/**
	 * Return the GrantedAccess object to perform the client login
	 *
	 * @param clientLogin String Client login (email)
	 * @return GrantedAccess
	 * @throws AuthException No Client found by the clientLogin provided
	 */
	public GrantedAccess login( final String clientLogin ) throws AuthException {
		final ClientEntity client = this.clientRepository.findClientByClientLogin( clientLogin );

		final GrantedAccess clientAcess = new GrantedAccess();

		clientAcess.setIdUsuario( client.getId() );
		clientAcess.setUserName( client.getName() );
		clientAcess.setPassword( client.getPassword() );
		clientAcess.setAccountExpirationDate( client.getPasswordExpiration() );
		clientAcess.setCredentialExpirationDate( client.getPasswordExpiration() );
		clientAcess.setActive( true );
		clientAcess.setEmail( client.getLogin() );
		clientAcess.setRole( client.getRole() );

		clientAcess.setUserAuthorities( this.findClientAuthorities( client ) );

		return clientAcess;
	}

	/**
	 * Returns a boolean value if the client has been logged out of the system
	 *
	 * @param token String token value
	 * @return boolean true -> client has been logged out. false -> client has not been logged out
	 */
	@Transactional( rollbackFor = AuthException.class )
	public boolean logout( final String token ) {
		try {
			final String clientLogin = this.getSubject( token );
			final ClientTokenEntity clientToken = this.findClientTokenByClientId( this.clientRepository.findClientByClientLogin( clientLogin ).getId() );

			if ( clientToken == null ) {
				return false;
			}

			clientToken.setActive( false );
			this.entityManager.persist( clientToken );

			return true;
		} catch ( final AuthException e ) {
			return false;
		}
	}

	/**
	 * Persists the newly created ClientTokenEntity
	 *
	 * @param client ClientEntity Client requesting a new token
	 * @param token String new requested/created token
	 * @param expirationDateTime LocalDateTime token expiration
	 */
	@Transactional( rollbackFor = AuthException.class )
	private void persistClientToken( final ClientEntity client, final String token, final LocalDateTime expirationDateTime ) {
		final ClientTokenEntity clientToken = new ClientTokenEntity();

		clientToken.setActive( true );
		clientToken.setClient( client );
		clientToken.setIssuedAt( LocalDateTime.now() );
		clientToken.setExpirationDate( expirationDateTime );
		clientToken.setToken( token );

		this.entityManager.persist( clientToken );

		this.entityManager.flush();
	}

	/**
	 * Validates either the token is valid or not
	 *
	 * @param token String token value
	 * @return GrantedAccess
	 */
	public GrantedAccess validateToken( final String token ) throws AuthException {
		try {
			final String clientLogin = this.getSubject( token );
			final ClientEntity client = this.clientRepository.findClientByClientLogin( clientLogin );

			if ( !this.isTokenLoginEqualToClientLogin( clientLogin, client ) ) {
				return null;
			}

			return this.login( clientLogin );
		} catch ( final AuthException e ) {
			throw new AuthBadRequestException( e.getMessage(), e );
		}
	}

}
