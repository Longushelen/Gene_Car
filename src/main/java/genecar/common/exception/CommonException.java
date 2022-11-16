package genecar.common.exception;

import lombok.Getter;

public class CommonException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter
  protected String errorCode = "9999";

  @Getter
  protected String message = "오류가 발생했습니다";

  @Getter
  private Object input = null;

  public CommonException(Object input) {
    super();
    this.input = input;
  }
}
