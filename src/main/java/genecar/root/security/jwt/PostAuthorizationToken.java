package genecar.root.security.jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import genecar.root.security.vo.AccountVO;

public class PostAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -4086609515300561602L;

    private PostAuthorizationToken(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(principal, credentials, authorities);
    }

    public static PostAuthorizationToken getTokenFormUserDetails(UserDetails userDetails) {

        return new PostAuthorizationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    public static PostAuthorizationToken getTokenFormUserDetails(AccountVO accountVO) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(
                new SimpleGrantedAuthority(accountVO.getRole())
        );

        return new PostAuthorizationToken(
                accountVO.getId(),
                "null password",
                grantedAuthorities
        );
    }

    public UserDetails getUserDetails() {

        return (UserDetails) super.getPrincipal();
    }
}