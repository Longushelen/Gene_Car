package genecar.root.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import genecar.root.common.service.CommonService;
import genecar.root.common.vo.PackingVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(originPatterns="*", allowCredentials="true")
@Controller
public class CommonController extends BaseController {
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("/api/menuCheck")
	@ResponseBody
    public PackingVO menuCheck(@RequestBody Map<String, Object> params) throws Exception {		
		PackingVO pack = getPack("", "", params);
		return pack;
	}
	
	/**
	 * 공통코드 조회
	 * 
	 * 2022-06-17
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/api/selectCode")
	@ResponseBody
    public PackingVO selectCode(@RequestBody Map<String, Object> params) throws Exception {		
		log.info("[ atprInfo ] param {}", params);
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		PackingVO pack;
		try {
			resultList = commonService.selectCode(params);
			
			pack = getPack("0000", "조회 성공", resultList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			pack = getPack("9999", "조회 실패", params);
		}
		
		return pack;
	}
	
}
