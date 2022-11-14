package genecar.member.service;

import org.springframework.stereotype.Service;

import genecar.member.mapper.MemberMapper;
import genecar.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberMapper memberMapper;

  public Boolean selectLogin(MemberVO paramVO) throws Exception {
    
    MemberVO vo = memberMapper.selectLogin(paramVO);
    
    if(vo.getMbrId().equals(paramVO.getMbrId()) && vo.getMbrPw().equals(paramVO.getMbrPw())) {
      return true;
    } else {
      return false;
    }
    
  }
  
}
