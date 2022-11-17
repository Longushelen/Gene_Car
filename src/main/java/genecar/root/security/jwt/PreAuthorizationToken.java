package genecar.root.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import genecar.root.security.vo.AccountFormVO;

public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 1578994683143401318L;

    private String reqAdmin;
    
	private PreAuthorizationToken(String username, String password) {
        super(username, password);
    }
	
    public PreAuthorizationToken(AccountFormVO vo) {
        this(vo.getId(), vo.getPassword());
        this.setReqAdmin(vo.getReqAdmin());
    }

    public String getUsername() {
        return (String) super.getPrincipal();
    }

    public String getUserPassword() {
        return (String) super.getCredentials();
    }
    
    public void setReqAdmin(String reqAdmin) {
    	this.reqAdmin = reqAdmin;
    }
    
    public String getReqAdmin() {
        return this.reqAdmin;
    }
}
