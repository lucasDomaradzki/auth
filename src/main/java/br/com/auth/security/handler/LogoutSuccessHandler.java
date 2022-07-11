package br.com.auth.security.handler;

import static br.com.auth.enums.HeaderType.AUTHORIZATION;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import br.com.auth.security.service.GrantedAccessService;

/**
 * <b>LogoutSuccessHandler</b> implements {@link org.springframework.security.web.authentication.logout.LogoutSuccessHandler} interface and handles the client logout of the system
 *
 * @author Lucas Domáradzkí
 */
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

	private final GrantedAccessService service;

	public LogoutSuccessHandler( final GrantedAccessService service ) {
		super();
		this.service = service;
	}

	/**
	 * Validates either the clienttoken is present in the request header or not
	 *
	 * @param request HttpServletRequest request
	 * @return boolean true -> token is present. false -> token is not present
	 */
	private boolean isHeaderTokenPresent( final HttpServletRequest request ) {
		return StringUtils.isNotEmpty( request.getHeader( AUTHORIZATION.name() ) );
	}

	@Override
	public void onLogoutSuccess( final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication ) throws IOException, ServletException {
		if ( !this.isHeaderTokenPresent( request ) ) {
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			response.getWriter().write( "O clienttoken deve ser informado para realizar o logout." );
			return;
		}

		if ( this.service.logout( request.getHeader( AUTHORIZATION.name() ) ) ) {
			response.setStatus( HttpServletResponse.SC_OK );
		} else {
			response.setStatus( HttpServletResponse.SC_GONE );
		}
	}

}
