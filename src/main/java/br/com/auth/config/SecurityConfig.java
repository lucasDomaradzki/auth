package br.com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.auth.password.AuthPasswordEncoder;
import br.com.auth.security.filter.AuthenticationFilter;
import br.com.auth.security.filter.LoginFilter;
import br.com.auth.security.handler.LogoutSuccessHandler;
import br.com.auth.security.service.AuthService;
import br.com.auth.security.service.GrantedAccessService;

/**
 * <b>SecurityConfig</b> extends {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter} and is the System core configuration about anything relating Security, Cors and Http Protocol, as well as adding Filters to Authentication and Login
 *
 * @author Lucas Domáradzkí
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GrantedAccessService service;

	@Override
	protected void configure( final AuthenticationManagerBuilder auth ) throws Exception {
		auth.userDetailsService( new AuthService( this.service ) ).passwordEncoder( new AuthPasswordEncoder() );
	}

	@Override
	protected void configure( final HttpSecurity http ) throws Exception {
		http.cors().configurationSource( this.getCors() );

		//FIXME @Lucas Domáradzkí - Https Protocol do be studied and implemented
		http.csrf().disable();

		http.authorizeRequests().antMatchers( "/login", "/client/new", "/client/password", "/client/password/forgot" ).permitAll();

		http.authorizeRequests().anyRequest().authenticated();

		http.logout().logoutUrl( "/logout" ).logoutSuccessHandler( new LogoutSuccessHandler( this.service ) );

		final AuthenticationFilter authenticationFilter = new AuthenticationFilter( this.service );
		http.addFilterBefore( authenticationFilter, BasicAuthenticationFilter.class );

		final LoginFilter loginFilter = new LoginFilter( this.authenticationManager(), this.service, this.objectMapper );
		http.addFilterBefore( loginFilter, BasicAuthenticationFilter.class );
	}

	/**
	 * Returns the Cors Configuration
	 *
	 * @return CorsConfigurationSource Cors Configuration
	 */
	private CorsConfigurationSource getCors() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials( true );
		config.addAllowedOrigin( CorsConfiguration.ALL );
		config.addAllowedHeader( CorsConfiguration.ALL );
		config.addAllowedMethod( CorsConfiguration.ALL );

		source.registerCorsConfiguration( "/**", config );

		return source;
	}

}
