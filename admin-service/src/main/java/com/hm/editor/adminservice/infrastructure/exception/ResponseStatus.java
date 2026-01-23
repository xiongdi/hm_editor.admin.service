package com.hm.editor.adminservice.infrastructure.exception;

public enum ResponseStatus {
    SUCCESS(10000, "请求成功"),
    FAILED(10001, null),
    UNAUTHORIZED_ERROR(10401, null),
    NOT_FOUND_ERROR(10404, "未找到接口"),
    ERROR(10500, "内部异常"),
    EMR_ERROR(10006, null);

    private final int code;
    private final String msg;

    ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
