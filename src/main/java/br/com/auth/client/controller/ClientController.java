package br.com.auth.client.controller;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.auth.client.domain.Client;
import br.com.auth.client.service.ClientService;
import br.com.auth.exception.AuthException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <b>ClientController</b>
 *
 * @author Lucas Domáradzkí
 */
@Api( value = "Client", tags = "Cliente" )
@RestController
@RequestMapping( "/client" )
public class ClientController {

	@Autowired
	private ClientService service;

	@ApiOperation( value = "Cria um novo Usuário" )
	@ApiResponses( value = { @ApiResponse( code = 201, message = "Novo Usuário criado com sucesso" ), @ApiResponse( code = 400, message = "Dados incorretos para criação de novo Usuário" ) } )
	@RequestMapping( value = "/new", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
	public ResponseEntity<Client> createNewClient( @RequestBody final Client client ) throws AuthException {
		return this.service.createNewClient( client );
	}

	@ApiOperation( value = "Consulta informações sobre Usuário pelo seu Token" )
	@ApiResponses( value = { @ApiResponse( code = 200, message = "Novo Usuário criado com sucesso" ), @ApiResponse( code = 403, message = "Não autorizado" ) } )
	@PreAuthorize( "hasAuthority('CONSULTAR_USUARIO')" )
	@RequestMapping( method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE )
	public ResponseEntity<Client> findOnlineClientInfo() {
		return this.service.findOnlineClientInfo();
	}

	@ApiOperation( value = "Atualiza o role do Usuário" )
	@ApiResponses( value = { @ApiResponse( code = 204, message = "Role do Usuário atualiza com sucesso" ), @ApiResponse( code = 403, message = "Não autorizado" ), @ApiResponse( code = 404, message = "Usuário não encontrado" ) } )
	@PreAuthorize( "hasAuthority('ADMIN_SISTEMA')" )
	@RequestMapping( value = "/role", method = RequestMethod.PATCH, consumes = APPLICATION_JSON_VALUE )
	public ResponseEntity<?> updateClientRole( @RequestBody final Client client ) throws AuthException {
		return this.service.updateClientRole( client );
	}

	@ApiOperation( value = "Atualiza a senha do Usuário" )
	@ApiResponses( value = { @ApiResponse( code = 204, message = "Senha atualizada com sucesso" ), @ApiResponse( code = 404, message = "Usuário não encontrado" ) } )
	@RequestMapping( value = "/password", method = RequestMethod.PATCH, consumes = APPLICATION_JSON_VALUE )
	public ResponseEntity<?> updateClientPassword( @RequestBody final Client client ) throws AuthException {
		return this.service.updateClientPassword( client );
	}

	@ApiOperation( value = "Gera uma nova senha para usuário" )
	@ApiResponses( value = { @ApiResponse( code = 204, message = "Senha atualizada com sucesso" ), @ApiResponse( code = 404, message = "Usuário não encontrado" ) } )
	@RequestMapping( value = "/password/forgot", method = RequestMethod.PATCH, consumes = APPLICATION_FORM_URLENCODED_VALUE )
	public ResponseEntity<?> forgotMypassword( @RequestParam final Map<String, String> requestParam ) throws AuthException {
		this.service.forgotMypassword( requestParam );
		return ResponseEntity.status( HttpStatus.NO_CONTENT ).build();
	}

}
