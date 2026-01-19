package com.hm.editor.adminservice.utils;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.common.utils.LogUtils;
import com.hm.editor.exception.BusinessException;
import com.hm.editor.exception.ResponseStatus;
import java.util.UUID;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/** 全局异常处理 */
@ControllerAdvice
public class ExceptionHandle {

  @ExceptionHandler(value = BusinessException.class)
  @ResponseBody
  public ApiResult bussinessException(BusinessException e) {
    return build(ResponseStatus.EMR_ERROR, e, logid());
  }

  @ExceptionHandler(value = NoHandlerFoundException.class)
  @ResponseBody
  public ApiResult noHandler(NoHandlerFoundException e) {
    return build(ResponseStatus.NOT_FOUND_ERROR, e, logid());
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ApiResult exception(Exception e) {
    return build(ResponseStatus.ERROR, e, logid());
  }

  @ExceptionHandler(value = Throwable.class)
  @ResponseBody
  public ApiResult throwable(Throwable e) {
    return build(ResponseStatus.ERROR, e, logid());
  }

  private String logid() {
    return UUID.randomUUID().toString();
  }

  private ApiResult build(ResponseStatus rs, Throwable e, String logid) {
    if (logid != null) {
      LogUtils.info("{},{}", logid, e);
    }

    ApiResult result = logid == null ? ApiResult.build(rs) : ApiResult.buildId(rs, logid);
    result.setException(e);
    if (rs.getMsg() == null) {
      result.setMsg(e == null ? "" : e.getMessage());
    }
    return result;
  }
}
