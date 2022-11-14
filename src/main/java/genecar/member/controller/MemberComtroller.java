package genecar.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import genecar.member.service.MemberService;
import genecar.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberComtroller {

  @Autowired
  MemberService memberService;
  
  @PostMapping("/api/login")
  @ResponseBody
  public Boolean joinUser(@RequestBody MemberVO paramVO) throws Exception {
	  
	  log.info("[ joinUser ] param {}", paramVO);  
	  return memberService.selectLogin(paramVO);
  }
}
