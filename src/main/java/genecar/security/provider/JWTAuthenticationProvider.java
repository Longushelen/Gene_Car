package genecar.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import genecar.security.jwt.JwtDecoder;
import genecar.security.jwt.JwtPreProcessingToken;
import genecar.security.jwt.PostAuthorizationToken;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        // Authorization : Bearer ~~  중 JWT 토큰 부분
        String token      = (String) authentication.getPrincipal();
        // Bearer 뒤 공백이 앞에 같이 있어서 trim 해서 사용
        return PostAuthorizationToken.getTokenFormUserDetails(jwtDecoder.decodeJwt(token.trim()));

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}