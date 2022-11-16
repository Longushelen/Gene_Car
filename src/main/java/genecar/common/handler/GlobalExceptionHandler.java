package genecar.common.handler;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import genecar.common.exception.CommonException;
import genecar.common.exception.NoDataException;
import genecar.common.util.PackingUtil;
import genecar.common.vo.PackingVO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {CommonException.class})
  protected ResponseEntity<PackingVO> handleCommonException(
      NoDataException ex, WebRequest request) {

    log.error("오류 처리 : {}", ex.getMessage());

    PackingVO packingVO = PackingUtil.getPack(request, ex.getErrorCode(), ex.getMessage(), ex.getInput(), "");

    return new ResponseEntity<>(packingVO, HttpStatus.BAD_REQUEST);
  }


}
