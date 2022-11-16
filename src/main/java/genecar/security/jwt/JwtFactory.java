package genecar.security.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import genecar.security.SecurityConst;
import genecar.security.vo.AuthVO;

@Component
public class JwtFactory {

  private static final Logger log = LoggerFactory.getLogger(JwtFactory.class);

  public String generateToken(AuthVO userDetails) {
    String token = null;
    try {
    	// 사용안함
//      Set<String> roles = userDetails.getAuthorities().stream()
//          .map(r -> r.getAuthority()).collect(Collectors.toSet());
//      String role = roles.iterator().next();

      token = JWT.create()
          .withIssuer("sangs")
          .withClaim("USERNAME", userDetails.getUsername())
          .withClaim("username", userDetails.getUsername())
          .withClaim("USER_ROLE", userDetails.getRole())
//          .withClaim("USER_PRIVILEGE", userDetails.getPrivilegesForUI())
          // 유효기간 1주일
          .withClaim("exp", new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15))
//          .withClaim("exp", new Date(System.currentTimeMillis() + 1000 * 60))
          .sign(generateAlgorithm());
    } catch (Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }

    return token;
  }

  private Algorithm generateAlgorithm() throws UnsupportedEncodingException {
    return Algorithm.HMAC256(SecurityConst.SECURITY_KEY);
  }
}
