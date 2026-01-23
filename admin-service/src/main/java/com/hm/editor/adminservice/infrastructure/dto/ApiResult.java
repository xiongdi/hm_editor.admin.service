package com.hm.editor.adminservice.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hm.editor.adminservice.infrastructure.exception.BusinessException;
import com.hm.editor.adminservice.infrastructure.exception.ResponseStatus;
import java.io.Serializable;
import java.util.Date;

public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 83291847898L;
    public static final int SUCCESS_CODE = ResponseStatus.SUCCESS.getCode();
    public static final int COMMON_FAILED_CODE = ResponseStatus.FAILED.getCode();

    private int code;
    private String msg;

    @JsonProperty("data")
    private T data;

    private Long stamp;
    // 日志标识
    private String id;
    private Throwable exception;

    public static ApiResult<Object> success() {
        return success("", "");
    }

    public static <T> ApiResult<T> success(T data) {
        return success("", data);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(SUCCESS_CODE);
        result.setMsg(message);
        result.setStamp(new Date().getTime());
        result.setData(data);
        return result;
    }

    public static ApiResult<Object> failed() {
        throw new BusinessException("");
    }

    public static ApiResult<Object> failed(String message) {
        return failed(COMMON_FAILED_CODE, message);
        //        throw new BusinessException(message);
    }

    public static ApiResult<Object> failed(int code, String message) {
        ApiResult<Object> result = new ApiResult<>();
        result.setCode(code);
        result.setMsg(message);
        result.setStamp(new Date().getTime());
        result.setData("");
        return result;
    }

    public static ApiResult<Object> build(ResponseStatus rs) {
        return build(rs, null, null);
    }

    public static ApiResult<Object> buildId(ResponseStatus rs, String id) {
        return build(rs, id, null);
    }

    public static ApiResult<Object> buildMsg(ResponseStatus rs, String msg) {
        return build(rs, null, msg);
    }

    public static ApiResult<Object> build(ResponseStatus rs, String id, String msg) {
        ApiResult<Object> r = new ApiResult<>();
        r.setCode(rs.getCode());
        r.setId(id);
        r.setMsg(msg == null || msg.isEmpty() ? rs.getMsg() : msg);
        return r;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
