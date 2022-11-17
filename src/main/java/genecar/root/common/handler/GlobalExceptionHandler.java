package genecar.root.common.handler;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import genecar.root.common.exception.CommonException;
import genecar.root.common.exception.NoDataException;
import genecar.root.common.util.PackingUtil;
import genecar.root.common.vo.PackingVO;

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
