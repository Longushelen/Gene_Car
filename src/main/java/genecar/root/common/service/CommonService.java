package genecar.root.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import genecar.root.common.mapper.CommonMapper;
import genecar.root.common.vo.WorkHistVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService {
	
	@Autowired
	private CommonMapper commonMapper;
	
	/**
	 * 공통코드 조회
	 * 
	 * 2022-06-17
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectCode(Map<String, Object> params) throws Exception{
		
		log.info("[ 공통코드 조회 ]");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {
			resultList = commonMapper.selectCode(params);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	/**
	 * SEQ 조회
	 * 
	 * 2022-06-17
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String selectSeq(String id, int len) throws Exception{
		
		log.info("[ SEQ 조회 ]");
		log.info("[ SEQ 조회 ID ] : " + id);
		
		String seq = "";
		String preSeq = "";
		
		try {
			seq = commonMapper.selectSeq(id);

			if(len > 0) {
				for (int i = 0; i < len; i++) {
					preSeq += 0;
				}
				seq = (preSeq + seq).trim();
			}
			
			log.info("[ SEQ 값 ] : " + seq);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return seq;
	}

	/**
	 * 관리자 작업이력
	 * @param workHistVO
	 * @return
	 */
	public int insertAdminWorkHist(WorkHistVO workHistVO) {
		
		int result = 0;
		
		try {
			result = commonMapper.insertAdminWorkHist(workHistVO);
		} catch (Exception e) {
			log.error("[ 관리자 작업이력 등록 오류 ]" + e.getMessage());
		}
		
		return result;
		
	}

}
