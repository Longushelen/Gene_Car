package genecar.root.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
  /**
   * 현재 인증상황 확인하여 인증한 회원 아이디(토큰 중에 username, DB 중에 mbrId로 사용하는..) 확인
   *
   * @return
   */
  public static String getUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null ? authentication.getName() : null;
  }

  /**
   * 적절한 권한이 있는지 확인
   *
   * @param authority 확인할 권한("ROLE_" 을 앞에 붙여야 한다. ROLE_USER, ROLE_ADMIN)
   * @return 권한을 가지고 있으면 true
   */
  public static boolean hasAuthority(String authority) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    return authentication.getAuthorities().contains(new SimpleGrantedAuthority(authority));
  }
}
