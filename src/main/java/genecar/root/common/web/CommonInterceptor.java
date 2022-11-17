package genecar.root.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import genecar.root.common.util.CmmnConst;
import genecar.root.common.util.CmmnUtil;
import genecar.root.common.vo.PackingVO;

@Component("commonInterceptor")
public class CommonInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		logger.info("CommonInterceptor [{}] 진입", request.getRequestURI());
		
		PackingVO pack = new PackingVO();
		pack.setPath(request.getRequestURI());
		pack.setTimestamp(CmmnUtil.yyyyMMddhhmmssSSS());
		request.setAttribute(CmmnConst.PACK, pack);
		return true;
	}
	
	
}