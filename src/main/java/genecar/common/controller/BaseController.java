package genecar.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import genecar.common.util.CmmnConst;
import genecar.common.vo.PackingVO;

public class BaseController {

	protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	protected HttpServletRequest request;
	
	@Autowired(required=true)
	protected HttpServletResponse response;
	
	@Autowired
    protected MessageSource messageSource;
	
	private PackingVO getPack(String id, String code, String msg, String msgDetail, Object input, Object output) {
		PackingVO tmp = null;
		try {
			tmp = (PackingVO) request.getAttribute(CmmnConst.PACK);
		} catch (Exception e) {
			return null;
		}
		
		tmp.setId(id);
		tmp.setCode(code);
		tmp.setMsg(msg);
		tmp.setMsgDetail(msgDetail);
		tmp.setInput(input);
		tmp.setOutput(output);
		
		return tmp;
	}
	
	protected PackingVO getPack() {
		return getPack("", "", "", "", null, null);
	}
	
	protected PackingVO getPack(String code, String msg, Object in) {
		return getPack("", code, msg, "", in, null);
	}
	
	protected PackingVO getPack(String code, String msg, Object in, Object out) {
		return getPack("", code, msg, "", in, out);
	}
	
}
