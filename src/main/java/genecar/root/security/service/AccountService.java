package genecar.root.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import genecar.root.security.mapper.AuthMapper;
import genecar.root.security.vo.AuthVO;
import genecar.root.security.vo.UserRole;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

	private final AuthMapper authMapper;
	
	@Override
	public AuthVO loadUserByUsername(String id) throws UsernameNotFoundException {
    	AuthVO auth = authMapper.loadUserByUsername(id);
    	
        /**
         * id 값이 존재하지 않는 경우
         * UsernameNotFoundException 에러 메소드를 사용
         * */
        if (auth !=null && auth.getUsername() != null) {
        	auth.setRole(UserRole.USER.getValue());
            return auth;
        } else {
            throw new UsernameNotFoundException("ID: " + id + "를 찾을 수 없습니다.");
        }
    }
	
	// 관리자 로그인
	public AuthVO loadUserByAdmin(String id) throws UsernameNotFoundException {
    	AuthVO auth = authMapper.loadAdminByUsername(id);
        /**
         * id 값이 DB 에 존재하지 않는 경우
         * UsernameNotFoundException 에러 메소드를 사용
         * */
        if (auth !=null && auth.getUsername() != null) {
        	auth.setPrivileges(authMapper.getPrivileges(auth.getRole()));
            return auth;
        } else {
            throw new UsernameNotFoundException("ID: " + id + "를 찾을 수 없습니다.");
        }
    }

}
