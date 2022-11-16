package genecar.security.vo;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@ToString
@Alias("AuthVO")
public class AuthVO implements UserDetails { // UserPrincipal

	private static final long serialVersionUID = 5214446943919489356L;
	private String username; // ID
	private String password;
	private String role;
	private String mbrNm; // 이름
	private Collection<? extends GrantedAuthority> roles;
	private Collection<? extends PrivilegeVO> privileges; // 권한
	// private boolean enabled;

	public void encodePassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(roles);
		return authList;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Collection<? extends PrivilegeVO> getPrivileges() {
		ArrayList<PrivilegeVO> privilegeList = new ArrayList<PrivilegeVO>(privileges);
		return privilegeList;
	}

}
