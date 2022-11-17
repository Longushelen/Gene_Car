package genecar.commercial.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import genecar.commercial.member.service.MemberService;
import genecar.commercial.member.vo.MemberVO;
import genecar.root.common.controller.BaseController;
import genecar.root.common.vo.PackingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController extends BaseController{

  @Autowired
  MemberService mbrService;
  
  @PostMapping("/api/mbr/info")
  @ResponseBody
  public PackingVO checkRecommenderId(@RequestBody MemberVO memberVo) throws Exception {
      return getPack("0000", " [회원정보 조회] ", memberVo, mbrService.mbrInfo(memberVo));
  }  
}
