package genecar.root.common.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Alias("WorkHistVO")
public class WorkHistVO {
	String workerId;
	String menuId;
	String menuNm;
	String resourceId;
	String resourceNm;
	String workDetail;
	String explan;
	String userIp;
}
