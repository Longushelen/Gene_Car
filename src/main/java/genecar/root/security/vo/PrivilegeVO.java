package genecar.root.security.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("PrivilegeVO")
public class PrivilegeVO {
	
	private String roleId;
	private String menuId;
	private String menuNm;
	private String menuPath;
	private String resourceCd;
	private String resourceNm;
	private String resourcePath;

}
