package genecar.root.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtPreProcessingToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -7330108801340040707L;

	private JwtPreProcessingToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public JwtPreProcessingToken(String token) {
		this(token, token.length());
	}
}
