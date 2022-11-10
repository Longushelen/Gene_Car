package genecar.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import genecar.member.vo.MemberVO;

@Mapper
public interface MemberMapper {

  MemberVO selectLongin(String mbrId, String mbrPw) throws Exception;
    
}
