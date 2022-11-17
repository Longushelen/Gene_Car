package genecar.root.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import genecar.root.common.service.CommonService;
import genecar.root.common.util.CmmnConst;
import genecar.root.common.util.CmmnUtil;
import genecar.root.common.vo.PackingVO;
import genecar.root.common.vo.WorkHistVO;
import genecar.root.security.service.AccountService;
import genecar.root.security.util.SecurityUtil;
import genecar.root.security.vo.AuthVO;
import genecar.root.security.vo.PrivilegeVO;
import lombok.RequiredArgsConstructor;

@Component("authCheckInterceptor")
@RequiredArgsConstructor
public class AuthCheckInterceptor implements HandlerInterceptor {

	private final AccountService accountService;
	
	private final CommonService commonService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthCheckInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String regURI = request.getRequestURI();
		
		logger.info("AuthCheckInterceptor [{}] 진입", regURI);
		
		AuthVO userDetails = this.accountService.loadUserByAdmin(SecurityUtil.getUsername());
		PackingVO pack = new PackingVO();
		pack.setPath(regURI);
		
		if (userDetails.getPrivileges().size() == 0 ) {
			logger.error("AuthCheckInterceptor Privileges 없음");
			pack.setCode("Auth_9910");
			pack.setMsg("권한이 없습니다.");
            return false; 
		}

		for (PrivilegeVO privilege : userDetails.getPrivileges()) {
			// url 뒤에 /:pk 가 붙으면 equals 로 조회할 수 없어서 시작부분 일치로 변경
			if (
					regURI.startsWith(privilege.getMenuPath()) ||
					regURI.startsWith(privilege.getResourcePath())
			) {
						// logger.info("AuthCheckInterceptor Privileges [{}]", userDetails.getPrivileges().toString());
						request.setAttribute("_privilegeVO", privilege);
						return true;
			}
		}

		pack.setCode("Auth_9920");
		pack.setMsg("권한이 없습니다.(9920)");
		logger.error("AuthCheckInterceptor Privileges 불충분");
		
		request.setAttribute("_privilegeVO", pack);
		// FIXME: ADMIN ROLE 이 없다면 위에서 throw 하기 때문에 최소한의 보안 체크는 가능. 개발 기간 중 privilege 문제 있어도 일단 true 로 함. privilege 체크 기능 다 작성하면 false 로 변경해야 함.
		return false;
	}
	
	// @Profile("prod") 
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
		Object obj = request.getAttribute("_privilegeVO");
		if ((obj != null) && (obj instanceof PrivilegeVO)) {
			PrivilegeVO privilegeVO = (PrivilegeVO) obj;
			WorkHistVO workHistVO = new WorkHistVO();
			workHistVO.setWorkerId(SecurityUtil.getUsername());
			workHistVO.setMenuId(privilegeVO.getMenuId());
			workHistVO.setMenuNm(privilegeVO.getMenuNm());
			workHistVO.setResourceId(privilegeVO.getResourceCd());
			workHistVO.setResourceNm(privilegeVO.getResourceNm());
			
			PackingVO packingVO = (PackingVO) request.getAttribute(CmmnConst.PACK);
			String workDetail = packingVO.getInput() == null ? "" : packingVO.getInput().toString();
			if (workDetail.length() > 2000) {
				workDetail = workDetail.substring(0, 2000);
			}
			workHistVO.setWorkDetail(workDetail);
			workHistVO.setExplan(packingVO.getMsg());
			workHistVO.setUserIp(CmmnUtil.getUserIp());
			this.commonService.insertAdminWorkHist(workHistVO);
		}
	}	

}
