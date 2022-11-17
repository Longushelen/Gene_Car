package genecar.commercial.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import genecar.commercial.member.vo.MemberVO;

@Mapper
public interface MemberMapper {
  MemberVO lookupMember(MemberVO paramVO) throws Exception;
}
