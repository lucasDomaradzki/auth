package br.com.auth.security.filter;

import static br.com.auth.enums.HeaderType.AUTHORIZATION;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.auth.access.GrantedAccess;
import br.com.auth.exception.AuthException;
import br.com.auth.response.AuthResponse;
import br.com.auth.security.service.GrantedAccessService;

/**
 * <b>JWTAuthenticationFilter</b> main Authentication Filter which watch every request and validates the <b>Authorization</b> upon checking if it's present in the request header
 *
 * @author Lucas Domáradzkí
 */
@Component
@Order( 1 )
public class AuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private final GrantedAccessService service;

	public AuthenticationFilter( final GrantedAccessService service ) {
		super();
		this.service = service;
	}

	@Override
	protected void doFilterInternal( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain ) throws ServletException, IOException {
		final Optional<String> token = Optional.ofNullable( request.getHeader( AUTHORIZATION.name() ) );
		if ( !token.isPresent() ) {
			filterChain.doFilter( request, response );
			return;
		}

		try {
			final GrantedAccess clientAccess = this.service.validateToken( StringUtils.substringAfter( token.get(), BEARER ) );

			if ( clientAccess == null ) {
				response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
				return;
			}

			clientAccess.setToken( token.get() );

			SecurityContextHolder.getContext().setAuthentication( new UsernamePasswordAuthenticationToken( clientAccess, null, clientAccess.getUserAuthorities() ) );
			filterChain.doFilter( request, response );
			return;
		} catch ( final AuthException e ) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write( new AuthResponse( HttpStatus.UNAUTHORIZED, request, e ).toString() );
			response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
			return;
		}
	}
}
