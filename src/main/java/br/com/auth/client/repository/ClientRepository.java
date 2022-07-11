package br.com.auth.client.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.auth.client.domain.Client;
import br.com.auth.client.entity.ClientEntity;
import br.com.auth.exception.AuthException;
import br.com.auth.exception.AuthMultipleResultsException;
import br.com.auth.exception.AuthNotFoundException;
import br.com.auth.password.AuthPasswordEncoder;
import br.com.auth.response.AuthResponse;
import br.com.auth.systemparams.repository.SystemParamRepository;

/**
 * <b>ClientRepository</b>
 *
 * @author Lucas Domáradzkí
 */
@Repository
public class ClientRepository {

	private static final String PASSWORDEXPIRATION = "client.password.expiration";

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SystemParamRepository systemRepository;

	/**
	 * Creates a new Client
	 *
	 * @param client Client from the request
	 * @return ClientEntity new Client created
	 * @throws AuthException Error creating new ClientEntity
	 */
	public ClientEntity createNewClient( final Client client ) throws AuthException {
		final ClientEntity clientEntity = this.getClientEntity( client );
		this.persistClientEntity( clientEntity );

		client.setId( clientEntity.getId() );
		client.setPassword(null);

		return clientEntity;
	}

	/**
	 * Find ClientEntity by its id
	 *
	 * @param id Integer Client ID
	 * @return ClientEntity
	 */
	private ClientEntity findClientByClientId( final Integer id ) {
		return this.entityManager.find( ClientEntity.class, id );
	}

	/**
	 * Return the ClientEntity
	 *
	 * @param client Client
	 * @return ClientEntity
	 * @throws AuthException No Client found
	 */
	public ClientEntity findClientByClientLoginNameAndBirthday( final Client client ) throws AuthException {
		try {
			final String jpql = "FROM ClientEntity client WHERE client.login = :clientLogin AND name = :clientName";
			final TypedQuery<ClientEntity> query = this.entityManager.createQuery( jpql, ClientEntity.class );
			query.setParameter( "clientLogin", client.getLogin().toLowerCase() );
			query.setParameter( "clientName", client.getName() );

			return query.getSingleResult();
		} catch ( final NoResultException e ) {
			throw new AuthNotFoundException( "Nenhum Usuário cadastrado para o login enviado: " + client.getLogin(), e );
		}
	}

	/**
	 * Return the ClientEntity searching by its login value
	 *
	 * @param clientLogin String Client login
	 * @return ClientEntity
	 * @throws AuthException No Client found
	 */
	public ClientEntity findClientByClientLogin( final String clientLogin ) throws AuthException {
			final String jpql = "FROM ClientEntity client WHERE client.login = :clientLogin";
			final TypedQuery<ClientEntity> query = this.entityManager.createQuery( jpql, ClientEntity.class );
			query.setParameter( "clientLogin", clientLogin.toLowerCase() );

			final List<ClientEntity> clients = query.getResultList();

			if ( clients.isEmpty() ) {
				throw new AuthNotFoundException( "Nenhum Usuário cadastrado para o login enviado: " + clientLogin );
			}

			if ( clients.size() > 1 ) {
				throw new AuthMultipleResultsException( new AuthResponse( "Mais de um usuário encontrado para o login: " + clientLogin ) );
			}

			return clients.get( 0 );
	}

	/**
	 * Returns a ClientEntity from a Client domain data
	 *
	 * @param client Client domain from the request
	 * @param clientEntity Client
	 * @throws AuthException Error setting up the password expiration and returning the ClientEntity
	 */
	private ClientEntity getClientEntity( final Client client ) throws AuthException {

		final ClientEntity clientEntity = new ClientEntity();

		clientEntity.setActive( true );
		clientEntity.setLogin( StringUtils.trim( client.getLogin() ) );
		clientEntity.setName( client.getName() );
		clientEntity.setPassword( new AuthPasswordEncoder().encode( client.getPassword() ) );
		clientEntity.setPasswordExpiration( LocalDateTime.now().plusYears( this.systemRepository.findNumericParam( PASSWORDEXPIRATION ) ) );
		clientEntity.setRole( client.getRole() );
		clientEntity.setBirthday( client.getBirthday() );
		clientEntity.setCpf( client.getCpf() );
		clientEntity.setOrigin( client.getOrigin() );
		clientEntity.setNickname( client.getNickname() );

		return clientEntity;
	}

	/**
	 * Persists a new ClienteEntity
	 *
	 * @param clientEntity ClienteEntity to be persisted
	 */
	@Transactional( rollbackFor = AuthException.class )
	public void persistClientEntity( final ClientEntity clientEntity ) {
		this.entityManager.persist( clientEntity );

		clientEntity.setId( clientEntity.getId() );
	}

	/**
	 * Updates the Client Password
	 *
	 * @param client Client from request
	 * @param clientEntity ClientEntity to have the password updated
	 */
	@Transactional( rollbackFor = AuthException.class )
	public void updateClientPassword( final Client client, final ClientEntity clientEntity ) {
		clientEntity.setPassword( new AuthPasswordEncoder().encode( client.getPassword() ) );
		clientEntity.setPasswordExpiration( LocalDateTime.now().plusYears( this.systemRepository.findNumericParam( PASSWORDEXPIRATION ) ) );
		clientEntity.setUpdateDate( LocalDateTime.now() );
		clientEntity.setActive( true );

		this.entityManager.persist( clientEntity );
	}

	/**
	 * Updates the client Role
	 *
	 * @param client Client from request
	 * @throws AuthException ClientEntity not found
	 */
	public void updateClientRole( final Client client ) throws AuthException {
		final ClientEntity clientEntity = this.findClientByClientId( client.getId() );

		if ( clientEntity == null ) {
			throw new AuthNotFoundException( "Usuário não encontrado para o id: " + client.getId() );
		}

		clientEntity.setRole( client.getRole() );

		this.persistClientEntity( clientEntity );
	}

	@Transactional( rollbackFor = AuthException.class )
	public void createNewPasswordForClient( final ClientEntity client ) {
		final String newPassword = RandomStringUtils.randomAscii( 12 );
		client.setPassword( new AuthPasswordEncoder().encode( newPassword ) );
		client.setPasswordExpiration( LocalDateTime.now().plusYears( this.systemRepository.findNumericParam( PASSWORDEXPIRATION ) ) );

		this.entityManager.persist( client );
	}

	public ClientEntity findClientByClientLoginAndName(final String clientLogin, final String clientName) throws AuthException {
		try {
			final String jpql = "FROM ClientEntity client WHERE client.login = :clientLogin AND client.name = :clientName";
			final TypedQuery<ClientEntity> query = this.entityManager.createQuery( jpql, ClientEntity.class );
			query.setParameter( "clientLogin", clientLogin );
			query.setParameter( "clientName", clientName );

			return query.getSingleResult();
		} catch ( final NoResultException e ) {
			throw new AuthNotFoundException( "Nenhum Usuário cadastrado para o login: " + clientLogin + " e nome: " + clientName, e );
		}
	}

}
