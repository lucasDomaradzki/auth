package br.com.auth.systemparams.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.auth.systemparams.entity.SystemParamEntity;

/**
 * <b>SystemParamRepository</b>
 *
 * @author Lucas Domáradzkí
 */
@Repository
public class SystemParamRepository {

	@Autowired
	private EntityManager entityManager;

	/**
	 * Returns the System Param as Byte Array
	 * 
	 * @param serverSecret String Server Secret
	 * @return byte[] Param
	 */
	public byte[] findByteParam( final String serverSecret ) {
		return this.entityManager.find( SystemParamEntity.class, serverSecret ).getParam().getBytes();
	}

	/**
	 * Returns the System Param as Long value
	 * 
	 * @param serverSecret String Server Secret
	 * @return Long Param
	 */
	public Long findNumericParam( final String serverSecret ) {
		return Long.parseLong( this.entityManager.find( SystemParamEntity.class, serverSecret ).getParam() );
	}

	/**
	 * Returns the System Param as String value
	 * 
	 * @param serverSecret String Server Secret
	 * @return String Param
	 */
	public String findStringParam( final String serverSecret ) {
		return this.entityManager.find( SystemParamEntity.class, serverSecret ).getParam();
	}

}
