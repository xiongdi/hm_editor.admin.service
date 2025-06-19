package com.hm.editor.adminservice.utils;

import com.alibaba.fastjson.JSON;
import com.hm.editor.common.dto.ApiResult;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@EnableWebMvc
@Configuration
public class GlobalReturnData {
    @RestControllerAdvice
    class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
            if(body == null){
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    return ApiResult.success(body);
                } else {
                    if("text".equals(mediaType.getType())){
                        return JSON.toJSONString(ApiResult.success(body));
                    }
                    return body;
                }
            }
            if(body instanceof Resource){
                return body;
            }

            if (body instanceof ApiResult) {
                return body;
            }
            if(body instanceof String){
                return JSON.toJSONString(ApiResult.success(body));
            }
            return ApiResult.success(body);
        }
    }
}
