package genecar.common.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import genecar.common.exception.NoDataException;
import genecar.common.vo.PackingVO;
import genecar.member.mapper.MemberMapper;
import genecar.member.vo.MemberVO;
import genecar.security.jwt.JwtFactory;
import genecar.security.service.AccountService;
import genecar.security.vo.AuthVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController extends BaseController {

//  private final MemberService memberService;

  private final MemberMapper memberRepository;

  private final JwtFactory jwtFactory;

  private final AccountService accountService;

  /**
   * 가입 상태 확인. 인증하지 않은 기존 회원은 인증 절차를 진행해야 한다
   */
  @PostMapping("/api/auth/select/gbn")
  @ResponseBody
  public PackingVO selectGbn(
      //@RequestBody Map<String, Object> params, MemberVO memberVo)
  @RequestBody Map<String, Object> params)
      throws Exception {

    Map<String, Object> result = new HashMap<>();
    PackingVO pack = getPack("9999", "아이디 또는 패스워드가 틀렸습니다.", params, result);

    MemberVO member = new MemberVO();
//    MemberVO member = memberRepository.findById(params.get("mbrId").toString()).orElseThrow(
//        () -> new NoDataException(params)
//    );

    pack.setOutput(member.getSignUpGbn());
    return pack;
  }

  /**
   * 아이디 암호를 사용하여 로그인 처리(액세스 토큰 응답)
   *
   * FIXME: 로그인은 토큰만 응답하도록 변경. 정보 조회는 별도 API 사용/
   */
  @PostMapping("/api/auth/login")
  @ResponseBody
  //public PackingVO login(@RequestBody Map<String, Object> params, MemberVO memberVo)
  public PackingVO login(@RequestBody Map<String, Object> params)
      throws Exception {

    Map<String, Object> result = new HashMap<>();
    PackingVO pack = getPack("1001", "아이디 또는 패스워드가 틀렸습니다.", params, result);

    UserDetails userDetails = this.accountService.loadUserByUsername(
        params.get("mbrId").toString());

    // jwt 에 필요한 정보 추가

    result.put("accessToken", jwtFactory.generateToken((AuthVO) userDetails));

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

    // TODO: 정보 중의 아이디로 해당 회원 다시 조회하고 유효한지 확인(토큰은 유효해도 탈퇴하거나 했을 수 있으므로)

    // 유효한 회원이면 auth에 정보 담아서 토큰 발급
//    AuthVO authVO = new AuthVO();
    // TODO: role 확인 후 필요하면 추가
//    authVO.setRoles();
//    authVO.setUsername(username);

    // jwt 에 필요한 정보 추가
    Map<String, Object> result = new HashMap<>();
    result.put("accessToken", jwtFactory.generateToken((AuthVO) userDetails));

    pack = getPack("0000", "로그인성공", params, result);
    return pack;
  }

}
