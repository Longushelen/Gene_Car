package genecar.root.common.util;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import genecar.root.common.vo.PackingVO;

@RequiredArgsConstructor
public class PackingUtil {

  /**
   * Request Servlet 에서 Packing 정보 취득
   *
   * @param request
   * @param id
   * @param code
   * @param msg
   * @param msgDetail
   * @param input
   * @param output
   * @return
   */
  public static PackingVO getPack(
      HttpServletRequest request,
      String id,
      String code,
      String msg,
      String msgDetail,
      Object input,
      Object output) {
    PackingVO tmp = null;
    try {
      tmp = (PackingVO) request.getAttribute(CmmnConst.PACK);
    } catch (Exception e) {
      return null;
    }

    tmp.setId(id);
    tmp.setCode(code);
    tmp.setMsg(msg);
    tmp.setMsgDetail(msgDetail);
    if (input != null) {
      tmp.setInput(input);
    }
    tmp.setOutput(output);

    return tmp;
  }

  /**
   * Request Servlet 에서 Packing 정보 취득
   *
   * @param request
   * @param errorCode
   * @param message
   * @param input
   * @param output
   * @return
   */
  public static PackingVO getPack(
      HttpServletRequest request, String errorCode, String message, Object input, Object output) {
    return getPack(request, "", errorCode, message, "", input, output);
  }

  /**
   * Request Servlet 에서 Packing 정보 취득
   *
   * @param request
   * @param id
   * @param code
   * @param msg
   * @param msgDetail
   * @param input
   * @param output
   * @return
   */
  public static PackingVO getPack(
      WebRequest request,
      String id,
      String code,
      String msg,
      String msgDetail,
      Object input,
      Object output) {
    PackingVO tmp = null;
    try {
      tmp = (PackingVO) request.getAttribute(CmmnConst.PACK, RequestAttributes.SCOPE_REQUEST);
    } catch (Exception e) {
      return null;
    }

    tmp.setId(id);
    tmp.setCode(code);
    tmp.setMsg(msg);
    tmp.setMsgDetail(msgDetail);
    if (input != null) {
      tmp.setInput(input);
    }
    tmp.setOutput(output);

    return tmp;
  }

  /**
   * Request Servlet 에서 Packing 정보 취득
   *
   * @param request
   * @param errorCode
   * @param message
   * @param input
   * @param output
   * @return
   */
  public static PackingVO getPack(
      WebRequest request, String errorCode, String message, Object input, Object output) {
    return getPack(request, "", errorCode, message, "", input, output);
  }
}
