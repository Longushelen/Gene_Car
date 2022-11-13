package genecar.member.service;

import org.springframework.stereotype.Service;

import genecar.member.mapper.MemberMapper;
import genecar.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberMapper memberMapper;

  public Boolean selectLogin(String mbrId, String mbrPw) throws Exception {
    
    MemberVO vo = memberMapper.selectLongin(mbrId, mbrPw);
    
    if(vo.getMbrId() == mbrId && vo.getMbrPw() == mbrPw) {
      return true;
    } else {
      return false;
    }
    
  }
  
}
