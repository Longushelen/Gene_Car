package genecar.member.vo;

import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberVO {

  private String mbrId;
  private String mbrPw;
  private String mbrPolicy;
  private String address1;
  private String address2;
  private String zipCd;
  private String authCi;
  private String signUpGbn;
  private String cMbrNm;
  private String cSex;
  private String cPhoneNo;
  private String bCorpRegNo;
  private String bCorpCertCd;
  private LocalDateTime regDt;
  private LocalDateTime iUpdDt;
  private LocalDateTime admUpdDt;
  
  @Override
  public String toString() {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
