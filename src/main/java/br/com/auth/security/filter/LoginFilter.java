package br.com.auth.security.filter;

import static br.com.auth.enums.HeaderType.CLIENTLOGIN;
import static br.com.auth.enums.HeaderType.CLIENTPASSWORD;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.auth.access.GrantedAccess;
import br.com.auth.access.Token;
import br.com.auth.response.AuthResponse;
import br.com.auth.security.service.GrantedAccessService;

/**
 * <b>JWTLoginFilter</b> catches the request when matched path is <b>/login</b> and make the header params are present so the client can be looged in the system and a {@link br.com.auth.access.Token} can be returned
 *
 * @author Lucas Domáradzkí
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;

	private final GrantedAccessService service;

	public LoginFilter( final AuthenticationManager authenticationManager, final GrantedAccessService service, final ObjectMapper objectMapper ) {
		super( new AntPathRequestMatcher( "/login" ) );

		this.setAuthenticationManager( authenticationManager );
		this.service = service;
		this.objectMapper = objectMapper;
	}

	/**
	 * Verify if header params are present in the request
	 *
	 * @param request HttpServletRequest
	 * @return String Missing Header param for login
	 */
	private String areHeaderParamsPresent( final HttpServletRequest request ) {
		if ( StringUtils.isEmpty( request.getHeader( CLIENTLOGIN.name() ) ) ) {
			return "O [clientlogin] não foi informado.";
		}

		if ( StringUtils.isEmpty( request.getHeader( CLIENTPASSWORD.name() ) ) ) {
			return "O [clientpassword] não foi informado.";
		}

		return null;
	}

	@Override
	public Authentication attemptAuthentication( final HttpServletRequest request, final HttpServletResponse response ) throws AuthenticationException, IOException, ServletException {
		if ( this.areHeaderParamsPresent( request ) != null ) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
			response.getWriter().write( new AuthResponse( HttpStatus.UNAUTHORIZED, request, this.areHeaderParamsPresent( request ) ).toString() );
			return null;
		}

		return this.getAuthenticationManager().authenticate( new UsernamePasswordAuthenticationToken( request.getHeader( CLIENTLOGIN.name() ), request.getHeader( CLIENTPASSWORD.name() ), Collections.emptyList() ) );
	}

	@Override
	protected void successfulAuthentication( final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult ) throws IOException, ServletException {
		final GrantedAccess user = (GrantedAccess) authResult.getPrincipal();

		final Token token = this.service.generateToken( user.getIdUsuario() );

		response.setContentType( APPLICATION_JSON_VALUE );
		response.getWriter().write( this.objectMapper.writeValueAsString( token ) );
	}

	@Override
	protected void unsuccessfulAuthentication( final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed ) throws IOException, ServletException {
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		response.getWriter().write( new AuthResponse( HttpStatus.BAD_REQUEST, request, failed ).toString() );
	}
}
