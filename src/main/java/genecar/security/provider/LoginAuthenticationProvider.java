package genecar.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import genecar.security.jwt.PostAuthorizationToken;
import genecar.security.jwt.PreAuthorizationToken;
import genecar.security.service.AccountService;
import genecar.security.vo.AuthVO;

@Component
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {
	
	private final AccountService accountService;
	
	private final PasswordEncoder passwordEncoder;
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreAuthorizationToken token = (PreAuthorizationToken) authentication;

        String username = token.getUsername();
        String password = token.getUserPassword();
        String reqAdmin = token.getReqAdmin(); // admin 권한요청

        AuthVO userDetails = null;
        if (reqAdmin !=null && reqAdmin.equals("ADM")) {
        	userDetails = this.accountService.loadUserByAdmin(username); // 관리자
        } else {
        	userDetails = this.accountService.loadUserByUsername(username); // 사용자
        }
        
        if (isCorrectPassword(password, userDetails.getPassword())) {
            return PostAuthorizationToken.getTokenFormUserDetails(userDetails);
        }

        throw new BadCredentialsException("아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.");
    }

    // Provider 를 연결 해주는 메소드 PreAuthorizationToken 사용한 filter 를 검색 후 연결
    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthorizationToken.class.isAssignableFrom(authentication);
    }

    private boolean isCorrectPassword(String password, String accountPassword) {
        return this.passwordEncoder.matches(password, accountPassword);
    }
} 