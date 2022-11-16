package genecar.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import genecar.common.controller.BaseController;
import genecar.common.vo.PackingVO;
import genecar.member.vo.MemberVO;

@RestController
public class HelloController extends BaseController{
  
  @PostMapping("/api/login")
  public PackingVO joinUser() throws Exception {
            
      MemberVO vo = new MemberVO();
      vo.setCMbrNm("구혜진");
      return getPack("0000", "조회완료", vo,"결과");
  }
}
