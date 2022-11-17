package genecar.root.common.exception;

import lombok.Getter;

public class NoDataException extends CommonException {

  private static final long serialVersionUID = 1L;

  @Getter
  protected String errorCode = "9999";

  @Getter
  protected String message = "데이터가 없습니다";

//  @Getter
//  private Object input = null;

  public NoDataException(Object input) {
    super(input);
  }

}
