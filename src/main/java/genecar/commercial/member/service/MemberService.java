package genecar.commercial.member.service;

import org.springframework.stereotype.Service;
import genecar.commercial.member.mapper.MemberMapper;
import genecar.commercial.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberMapper memberMapper;
  
  public MemberVO mbrInfo(MemberVO paramVO) throws Exception {
    
    return memberMapper.lookupMember(paramVO);
    
  }
}
