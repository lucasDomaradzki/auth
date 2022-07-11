package br.com.auth.client.service;

import static br.com.auth.enums.ClientRole.CLIENTE;
import static br.com.auth.enums.ClientRole.GERENTE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.auth.access.GrantedAccess;
import br.com.auth.authority.repository.ClientAuthorityRepository;
import br.com.auth.client.domain.Client;
import br.com.auth.client.entity.ClientEntity;
import br.com.auth.client.repository.ClientRepository;
import br.com.auth.enums.HeaderType;
import br.com.auth.exception.AuthBadRequestException;
import br.com.auth.exception.AuthException;
import br.com.auth.exception.AuthNotFoundException;
import br.com.auth.password.AuthPasswordEncoder;
import br.com.auth.security.AbstractSecurityContext;

@Service
public class ClientServiceImpl extends AbstractSecurityContext implements ClientService {

	@Autowired
	private ClientAuthorityRepository clientAuthorityRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Override
	@Transactional( rollbackOn = AuthException.class )
	public ResponseEntity<Client> createNewClient( final Client client ) throws AuthException {
		try {
			this.validateClientRole( client );
			return this.validateClientAlreadyReported( client );
		} catch ( final AuthNotFoundException e ) {
			final ClientEntity clientEntity = this.clientRepository.createNewClient( client );
			this.clientAuthorityRepository.createInitialClientAuthority( clientEntity );
			return ResponseEntity.status(HttpStatus.CREATED).body( client );
		}
	}

	@Override
	public ResponseEntity<Client> findOnlineClientInfo() {
		final GrantedAccess grantedAccess = this.getGrantedAccess();

		final Client client = new Client();
		final List<String> authorities = new ArrayList<>();
		grantedAccess.getUserAuthorities().stream().forEach(authority -> {
			authorities.add(authority.getAuthority());
		});

		client.setAuthorities(authorities);

		client.setName(grantedAccess.getUsername());
		client.setLogin(grantedAccess.getEmail());
		client.setRole(grantedAccess.getRole());
		client.setId(grantedAccess.getIdUsuario());

		return ResponseEntity.status(HttpStatus.OK).body(client);
	}

	/**
	 * Validates either the old password matches the Database password stored for
	 * the Client
	 *
	 * @param client        Client
	 * @param clienteEntity ClientEntity
	 * @return boolean true -> passwords match - false -> passwords don't match
	 * @throws AuthException No Old Password provided
	 */
	private boolean isOldPasswordValid(final Client client, final ClientEntity clienteEntity) throws AuthException {
		if (client.getOldPassword() == null) {
			throw new AuthBadRequestException("Informe a senha antiga criar uma nova senha.");
		}

		return new AuthPasswordEncoder().matches(client.getOldPassword(), clienteEntity.getPassword());
	}

	@Override
	public ResponseEntity<?> updateClientPassword(final Client client) throws AuthException {
		final ClientEntity clientEntity = this.clientRepository.findClientByClientLogin( client.getLogin() );

		this.validateClientPassword(client, clientEntity);

		this.clientRepository.updateClientPassword(client, clientEntity);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<?> updateClientRole(final Client client) throws AuthException {
		this.clientRepository.updateClientRole(client);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * Validates if Client is already reported
	 *
	 * @param client Client
	 * @throws AuthException Client already reported
	 */
	private ResponseEntity<Client> validateClientAlreadyReported(final Client client) throws AuthException {
		final ClientEntity clientEntity = this.clientRepository.findClientByClientLoginNameAndBirthday( client );

		client.setId(clientEntity.getId());
		return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(client);
	}

	/**
	 * Validates the Client Password
	 *
	 * @param client       Client
	 * @param clientEntity ClientEntity
	 * @throws AuthException Password expiration still valid or passwords don't
	 *                       match
	 */
	private void validateClientPassword(final Client client, final ClientEntity clientEntity) throws AuthException {
		if (clientEntity.getPasswordExpiration().isAfter(LocalDateTime.now())) {
			final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			throw new AuthBadRequestException("A senha atual ainda tem expiração válida: " + clientEntity.getPasswordExpiration().format(dateFormat) + ".");
		}

		if (!this.isOldPasswordValid(client, clientEntity)) {
			throw new AuthBadRequestException("A senha anterior não é compatível.");
		}
	}

	/**
	 * Validates if Client Role sent is different from 'GERENTE' value
	 *
	 * @param client Client
	 * @throws AuthException Only CLIENTE Role is allowed to create a new Client
	 */
	private void validateClientRole( final Client client ) throws AuthException {
		if ( !client.getRole().equals( GERENTE ) ) {
			return;
		}

		throw new AuthBadRequestException( "A criação de novos usuários é permitida apenas para o tipo: " + CLIENTE.name() );
	}

	@Override
	public void forgotMypassword(final Map<String, String> requestParam) throws AuthException {
		final String clientLogin = requestParam.get(HeaderType.CLIENTLOGIN.getHeaderName());
		if (StringUtils.isEmpty(clientLogin)) {
			throw new AuthBadRequestException("Nenhum login de usuário informado");
		}

		// FIXME @lucas.domaradzki Ajustar funcionalidade esqueci minha senha
//		final ClientEntity client = this.clientRepository.findClientByClientLogin(clientLogin);

//		this.isClientStillActive(client);

//		this.clientRepository.createNewPasswordForClient(client);
	}

	private void isClientStillActive(final ClientEntity client) throws AuthBadRequestException {
		if (!client.isActive()) {
			throw new AuthBadRequestException("O usuário não está ativo no sistema.");
		}
	}

}
