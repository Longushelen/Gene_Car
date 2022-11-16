package genecar.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@NoArgsConstructor
public class PackingVO<T1 extends Object, T2 extends Object> {

  private String id = "";
  private String timestamp = "";
  private String path = "";
  private T1 input = null;
  private T2 output = null;
  private String code = "0000";
  private String msg = "";
  private String msgDetail = "";

  public PackingVO(
      String code, String msg, T1 input, T2 output) {
    this.code = code;
    this.msg = msg;
    this.input = input;
    this.output = output;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }


}
