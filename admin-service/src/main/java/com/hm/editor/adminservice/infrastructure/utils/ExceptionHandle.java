package com.hm.editor.adminservice.infrastructure.utils;

import com.hm.editor.adminservice.infrastructure.dto.ApiResult;
import com.hm.editor.adminservice.infrastructure.utils.LogUtils;
import com.hm.editor.adminservice.infrastructure.exception.BusinessException;
import com.hm.editor.adminservice.infrastructure.exception.ResponseStatus;
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
    public ApiResult<Object> businessException(BusinessException e) {
        return build(ResponseStatus.EMR_ERROR, e, logid());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public ApiResult<Object> noHandler(NoHandlerFoundException e) {
        return build(ResponseStatus.NOT_FOUND_ERROR, e, logid());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult<Object> exception(Exception e) {
        return build(ResponseStatus.ERROR, e, logid());
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public ApiResult<Object> throwable(Throwable e) {
        return build(ResponseStatus.ERROR, e, logid());
    }

    private String logid() {
        return UUID.randomUUID().toString();
    }

    private ApiResult<Object> build(ResponseStatus rs, Throwable e, String logid) {
        if (logid != null) {
            LogUtils.info("{},{}", logid, e);
        }

        ApiResult<Object> result =
            logid == null ? ApiResult.build(rs) : ApiResult.buildId(rs, logid);
        result.setException(e);
        if (rs.getMsg() == null) {
            result.setMsg(e == null ? "" : e.getMessage());
        }
        return result;
    }
}
