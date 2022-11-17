package genecar.root.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import genecar.root.common.vo.WorkHistVO;

@Mapper
public interface CommonMapper {

	/**
	 * SEQ 조회
	 * 
	 * 2022-06-17
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	String selectSeq(String params) throws Exception;
	
	/**
	 * 공통코드 조회
	 * 
	 * 2022-06-17
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectCode(Map<String, Object> params) throws Exception;

	/**
	 * 관리자 작업이력
	 * @param workHistVO
	 * @return
	 * @throws Exception
	 */
	int insertAdminWorkHist(WorkHistVO workHistVO) throws Exception;

}
