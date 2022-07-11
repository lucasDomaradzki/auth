package br.com.auth.authority.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.auth.authority.entity.AuthorityEntity;
import br.com.auth.exception.AuthException;
import br.com.auth.exception.AuthNotFoundException;

/**
 * <b>AuthorityRepository</b>
 *
 * @author Lucas Domáradzkí
 */
@Repository
public class AuthorityRepository {

	@Autowired
	private EntityManager entityManager;

	public AuthorityEntity findAuthorityByAuthorityName( final String authority ) throws AuthException {
		try {
			final String jpql = "FROM AuthorityEntity WHERE name = :authorityName AND active = :active";
			final TypedQuery<AuthorityEntity> query = this.entityManager.createQuery( jpql, AuthorityEntity.class );
			query.setParameter( "authorityName", authority );
			query.setParameter( "active", true );

			return query.getSingleResult();
		} catch ( final NoResultException e ) {
			throw new AuthNotFoundException( "Nenhuma Authority encontrada para o valor: " + authority );
		}
	}
}
