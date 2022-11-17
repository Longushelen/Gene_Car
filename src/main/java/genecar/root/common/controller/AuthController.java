package genecar.root.common.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import genecar.commercial.member.service.MemberService;
import genecar.commercial.member.vo.MemberVO;
import genecar.root.common.vo.PackingVO;
import genecar.root.security.jwt.JwtFactory;
import genecar.root.security.service.AccountService;
import genecar.root.security.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController extends BaseController {

  private final MemberService memberService;

  private final JwtFactory jwtFactory;
  
  private final AccountService accountService;

  /**
   * 아이디 암호를 사용하여 로그인 처리(액세스 토큰 응답)
   *
   * FIXME: 로그인은 토큰만 응답하도록 변경. 정보 조회는 별도 API 사용/
   */
  @PostMapping("/api/auth/login")
  @ResponseBody
  public PackingVO login(@RequestBody Map<String, Object> params, MemberVO memberVo)
      throws Exception {

    Map<String, Object> result = new HashMap<>();
    PackingVO pack = getPack("1001", "아이디 또는 패스워드가 틀렸습니다.", params, result);

    memberVo.setMbrId((String) params.get("mbrId"));
    memberVo.setMbrPw((String) params.get("mbrPw"));
    
    MemberVO vo = memberService.mbrInfo(memberVo);

    if(vo != null) {
      UserDetails userDetails 
      = this.accountService.loadUserByUsername( params.get("mbrId").toString());
      
      result.put("accessToken", jwtFactory.generateToken((AuthVO) userDetails));
      result.put("loginSuccess", "Y");
      result.put("mbrInfo", vo);
      params.put("urlCode", "0");
      pack = getPack("0000", "로그인 성공", params, result);
    }

    return pack;
  }

  /**
   * 유효한 액세스 토큰을 사용하여 액세스 토큰 재발급(유효기간 재설정)
   */
  @PostMapping("/api/auth/refreshLogin")
  @ResponseBody
  public PackingVO refreshLogin(
      HttpServletRequest req,
      @RequestBody(required = false) Map<String, Object> params
  )
      throws Exception {
    String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();

    PackingVO pack = getPack("0000", "아이디 또는 패스워드가 틀렸습니다.", params, null);

    // TODO: 토큰에서 이전 정보 다시 취득(정보 다시 사용해서 새로운 토큰 생성)
    UserDetails userDetails = this.accountService.loadUserByUsername(username);

    // jwt 에 필요한 정보 추가
    Map<String, Object> result = new HashMap<>();
    result.put("accessToken", jwtFactory.generateToken((AuthVO) userDetails));

    pack = getPack("0000", "로그인성공", params, result);
    return pack;
  }

}
