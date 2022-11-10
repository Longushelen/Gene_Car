package genecar.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import genecar.member.service.MemberService;
import genecar.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberComtroller {

  @Autowired
  MemberService memberService;
  
  @PostMapping("/login")
  @ResponseBody
  public Boolean joinUser(@RequestBody String mbrId, String mbrPw) throws Exception {
    return memberService.selectLogin(mbrId, mbrPw);
  }
}
