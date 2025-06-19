package com.hm.editor.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hm.editor.exception.BusinessException;
import com.hm.editor.exception.ResponseStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 83291847898L;
    public static final int SUCCESS_CODE = ResponseStatus.SUCCESS.getCode();
    public static final int COMMON_FAILED_CODE = ResponseStatus.FAILED.getCode();

    private int code;
    private String msg;
    @JSONField(serialzeFeatures= SerializerFeature.WriteMapNullValue,name="data")
    private T data;
    private Long stamp;
    // 日志标识
    private String id;
    private Throwable exception;

    public static ApiResult success() {
        return success("", "");
    }

    public static <T> ApiResult success(T data) {
        return success("", data);
    }

    public static <T> ApiResult success(String message, T data) {
        ApiResult result = new ApiResult();
        result.setCode(SUCCESS_CODE);
        result.setMsg(message);
        result.setStamp(new Date().getTime());
        result.setData(data);
        return result;
    }

    public static ApiResult failed() {
        throw new BusinessException("");
    }

    public static ApiResult failed(String message) {
        return failed(COMMON_FAILED_CODE, message);
//        throw new BusinessException(message);
    }

    public static ApiResult failed(int code, String message) {
        ApiResult result = new ApiResult();
        result.setCode(code);
        result.setMsg(message);
        result.setStamp(new Date().getTime());
        result.setData("");
        return result;
    }
    public static ApiResult build(ResponseStatus rs){
        return build(rs,null,null);
    }
    public static ApiResult buildId(ResponseStatus rs,String id){
        return build(rs,id,null);
    }
    public static ApiResult buildMsg(ResponseStatus rs,String msg){
        return build(rs,null,msg);
    }

    public static ApiResult build(ResponseStatus rs,String id,String msg){
        ApiResult r = new ApiResult();
        r.setCode(rs.getCode());
        r.setId(id);
        r.setMsg(msg == null || msg.length() == 0?rs.getMsg():msg);
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
