package br.com.auth.client.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import br.com.auth.client.domain.Client;
import br.com.auth.exception.AuthException;

/**
 * <b>ClientService</b>
 *
 * @author Lucas Domáradzkí
 */
public interface ClientService {

	/**
	 * Creates a new Client
	 *
	 * @param client Client
	 * @return ResponseEntity<Client>
	 * @throws AuthException Error creating new Client
	 */
	public ResponseEntity<Client> createNewClient( final Client client ) throws AuthException;

	/**
	 * Returns logged in Client info
	 *
	 * @return ResponseEntity<Cliente>
	 */
	public ResponseEntity<Client> findOnlineClientInfo();

	/**
	 * Creates a new password to the Client
	 *
	 * @param clientLogin Map<String, String> Client Login
	 * @throws AuthException Error generating new password
	 */
	public void forgotMypassword( final Map<String, String> client ) throws AuthException;

	/**
	 * Updates Client Password
	 *
	 * @param client Client
	 * @return ResponseEntity<?>
	 * @throws AuthException Error Updating Client Password
	 */
	public ResponseEntity<?> updateClientPassword( Client client ) throws AuthException;

	/**
	 * Updates Client Role
	 *
	 * @param client Client
	 * @return ResponseEntity<?>
	 * @throws AuthException Error updating Client Role
	 */
	public ResponseEntity<?> updateClientRole( final Client client ) throws AuthException;

}
