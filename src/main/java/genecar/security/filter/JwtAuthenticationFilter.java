package genecar.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import genecar.common.util.CmmnConst;
import genecar.common.util.CmmnUtil;
import genecar.common.vo.PackingVO;
import genecar.security.SecurityConst;
import genecar.security.jwt.JwtPreProcessingToken;

/**
 * Token 을 내려주는 Filter 가 아닌  client 에서 받아지는 Token 을 서버 사이드에서 검증하는 클레스 SecurityContextHolder 보관소에 해당
 * Token 값의 인증 상태를 보관 하고 필요할때 마다 인증 확인 후 권한 상태 확인 하는 기능
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}
	
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        // JWT 값을 담아주는 변수 TokenPayload
        String tokenPayload = request.getHeader(SecurityConst.AUTHORIZATION_HEADER);

        if (tokenPayload == null || tokenPayload.equals("") || tokenPayload.length() < SecurityConst.AUTH_HEADER_PREFIX.length()) {
        	throw new BadCredentialsException("Header Authorization is null");
        }
        String token = tokenPayload.substring(SecurityConst.AUTH_HEADER_PREFIX.length(), tokenPayload.length());
        JwtPreProcessingToken preAuthorizationToken = new JwtPreProcessingToken(token);

		return super.getAuthenticationManager().authenticate(preAuthorizationToken);
    }

    @Override
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		/*
		 * SecurityContext 사용자 Token 저장소를 생성 SecurityContext 에 사용자의 인증된 Token 값을 저장
		 */
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);

		// FilterChain chain 해당 필터가 실행 후 다른 필터도 실행할 수 있도록 연결실켜주는 메서드
		chain.doFilter(request, response);
	}

    @Override
	protected void unsuccessfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		/*
		 * 로그인을 한 상태에서 Token값을 주고받는 상황에서 잘못된 Token값이라면 인증이 성공하지 못한 단계 이기 때문에 잘못된 Token값을
		 * 제거합니다. 모든 인증받은 Context 값이 삭제됨
		 */
		SecurityContextHolder.clearContext();

		PackingVO pack = new PackingVO();
		pack.setPath(request.getRequestURI());
		pack.setTimestamp(CmmnUtil.yyyyMMddhhmmssSSS());
		pack.setCode(SecurityConst.ERROR_CD_JWT_UNSUCCESSFUL);
		pack.setMsg(failed.getMessage());
		
		request.setAttribute(CmmnConst.PACK, pack);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(SecurityConst.CHARACTER_ENCODING_UTF8);
		response.getWriter().write(new ObjectMapper().writeValueAsString(pack));
		
	}
}