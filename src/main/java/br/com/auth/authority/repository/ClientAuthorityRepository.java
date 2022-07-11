package br.com.auth.authority.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.auth.authority.entity.ClientAuthorityEntity;
import br.com.auth.client.entity.ClientEntity;
import br.com.auth.exception.AuthException;

/**
 * <b>ClientAuthorityRepository</b>
 *
 * @author Lucas Domáradzkí
 */
@Repository
public class ClientAuthorityRepository {

	// Represents the first permitted Client Authority upon new Client creation
	private static final String CONSULTAR_USUARIO = "CONSULTAR_USUARIO";

	@Autowired
	private AuthorityRepository autorithyRepository;

	@Autowired
	private EntityManager entityManager;

	public void createInitialClientAuthority( final ClientEntity clientEntity ) throws AuthException {
		final ClientAuthorityEntity clientAuthorityEntity = new ClientAuthorityEntity();

		clientAuthorityEntity.setActive( true );
		clientAuthorityEntity.setAuthority( this.autorithyRepository.findAuthorityByAuthorityName( CONSULTAR_USUARIO ) );
		clientAuthorityEntity.setClient( clientEntity );

		this.entityManager.persist( clientAuthorityEntity );
	}

}
