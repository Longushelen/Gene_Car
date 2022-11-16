package genecar.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import genecar.common.util.CmmnConst;
import genecar.common.util.CmmnUtil;
import genecar.common.vo.PackingVO;
import genecar.security.SecurityConst;
import genecar.security.jwt.JwtFactory;
import genecar.security.jwt.PostAuthorizationToken;
import genecar.security.jwt.PreAuthorizationToken;
import genecar.security.vo.AccountFormVO;
import genecar.security.vo.AuthVO;
import genecar.security.vo.TokenVO;

public class FormLoginFilter extends AbstractAuthenticationProcessingFilter {

	public FormLoginFilter(AntPathRequestMatcher matchUrl) {
		super(matchUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {

		// JSON 으로 변환
		AccountFormVO accountFormVO = new ObjectMapper().readValue(req.getReader(), AccountFormVO.class);

		// 사용자입력값이 존재하는지 비교
		PreAuthorizationToken token = new PreAuthorizationToken(accountFormVO);

		// PreAuthorizationToken 해당 객체에 맞는 Provider를
		// getAuthenticationManager 해당 메서드가 자동으로 찾아서 연결해 준다.
		// 자동으로 찾아준다고 해도 Provider 에 직접 PreAuthorizationToken 지정해 줘야 찾아갑니다.

		return super.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PostAuthorizationToken token   = (PostAuthorizationToken) authResult;
        AuthVO userDetails = (AuthVO) token.getUserDetails();

        String tokenString = new JwtFactory().generateToken(userDetails);

        TokenVO tokenVO = new TokenVO(tokenString, userDetails.getUsername());

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpStatus.OK.value());
        res.getWriter().write(new ObjectMapper().writeValueAsString(tokenVO));
    }

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException failed) throws IOException, ServletException {
		// response.sendRedirect("/denied"); // 페이지 만들면 리턴
    	PackingVO packVO = new PackingVO();
    	packVO.setPath(req.getRequestURI());
    	packVO.setTimestamp(CmmnUtil.yyyyMMddhhmmssSSS());
		packVO.setCode(SecurityConst.ERROR_CD_LOGIN_UNSUCCESSFUL);
		packVO.setMsg(failed.getMessage());
		
		req.setAttribute(CmmnConst.PACK, packVO);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		res.setCharacterEncoding(SecurityConst.CHARACTER_ENCODING_UTF8);
		res.getWriter().write(new ObjectMapper().writeValueAsString(packVO));
	}
}