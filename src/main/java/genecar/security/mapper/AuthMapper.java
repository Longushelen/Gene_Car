package genecar.security.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import genecar.security.vo.AuthVO;
import genecar.security.vo.PrivilegeVO;

/**
 * 권한
 *
 */
@Mapper
public interface AuthMapper {

	AuthVO loadUserByUsername(String id) throws UsernameNotFoundException;
	AuthVO loadAdminByUsername(String id) throws UsernameNotFoundException;
	List<PrivilegeVO> getPrivileges(String id);

}
