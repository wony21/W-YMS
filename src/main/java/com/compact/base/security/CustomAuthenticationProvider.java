package com.compact.base.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.compact.base.common.CommonData.ROLE;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	//@Autowired
	//UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userId = (String) authentication.getPrincipal();		
//		if ( userId.length() < 4) {
//			throw new InternalAuthenticationServiceException("Invalid userId value length(require length is 4 over)");
//		}
		if (userId.length() < 0) {
			throw new InternalAuthenticationServiceException("Invalid userId, that is null.");
		}
		String userPw = (String) authentication.getCredentials();
		logger.info("Login requsted : {}, {}", userId, userPw);
		CustomUserDetails userDetails;
		// 권한부여
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		if( userId.toLowerCase().equals("test") && userPw.toLowerCase().equals("test") ) {
			roles.add(new SimpleGrantedAuthority(ROLE.ADMIN));
			roles.add(new SimpleGrantedAuthority(ROLE.MANAGER));
			roles.add(new SimpleGrantedAuthority(ROLE.USER));
			userDetails = new CustomUserDetails();			
			userDetails.setUserCd(userId);
			userDetails.setUsername("테스트유저");
			userDetails.setUsername2("테스트유저");
			
		} else {
			// Get UserLv from Db
			// Set UserGrant from UserLv  
			throw new BadCredentialsException("Bad credentials [" + userId + "/" + userPw + "]");
		}
		//User userInfo = userService.getUserInfo(userId, userPw);
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userId, userPw, roles);
		result.setDetails(userDetails);
		return result;
		
	}
}