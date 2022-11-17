package genecar.commercial.member.vo;

import java.time.LocalDateTime;
import org.apache.ibatis.type.Alias;
import lombok.Data;

@Data
@Alias("MemberVO")
public class MemberVO {
  // 공통 회원 정보 <필수>
  private String mbrId;
  private String mbrPw;
  // 본인, 법인 인증
  private String authCi;
  
  // 공통 회원 정보
  private String address1;
  private String address2;
  private String zipCd;
  private String signUpGbn;
  private LocalDateTime regDt;
  private LocalDateTime iUpdDt;
  private LocalDateTime admUpdDt;
  
  // 개인
  private String cMbrNm;
  private String cSex;
  private String cPhoneNo;
  // 회사 
  private String bCorpRegNo;
  private String bCorpNm;
  private String bCorpCertCd;
  
}
