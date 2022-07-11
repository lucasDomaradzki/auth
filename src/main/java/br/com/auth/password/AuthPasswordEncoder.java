package br.com.auth.password;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <b>AuthPasswordEncoder</b> implements {@link org.springframework.security.crypto.password.PasswordEncoder}
 * and is used by the Login System Filter to match the request password to the stored Database password
 *
 * @author Lucas Domáradzkí
 */
public class AuthPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode( final CharSequence rawPassword ) {
		return DigestUtils.sha512Hex( rawPassword.toString() );
	}

	@Override
	public boolean matches( final CharSequence rawPassword, final String encodedPassword ) {
		return StringUtils.equals( this.encode( rawPassword ), encodedPassword );
	}

}
