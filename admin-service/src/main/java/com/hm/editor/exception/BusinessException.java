package com.hm.editor.exception;

/**
  * <p>业务异常</p>
  */
public class BusinessException extends RuntimeException{
    public BusinessException(){
        super();
    }

    public BusinessException(String message){
        super(message);
    }
    public BusinessException(String message, Throwable cause){
        super(message,cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
    public static void error(String msg){
        throw new BusinessException(msg);
    }

}
