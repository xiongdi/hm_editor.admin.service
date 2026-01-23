package com.hm.editor.adminservice.infrastructure.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hm.editor.adminservice.infrastructure.dto.ApiResult;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalReturnData implements ResponseBodyAdvice<Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(
        MethodParameter methodParameter,
        Class<? extends HttpMessageConverter<?>> aClass
    ) {
        if (methodParameter != null && methodParameter.getMethod() != null) {
            String className = methodParameter.getContainingClass().getName();
            if (
                className.contains("springdoc") ||
                className.contains("knife4j") ||
                className.contains("swagger") ||
                className.contains("openapi")
            ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(
        Object body,
        MethodParameter methodParameter,
        MediaType mediaType,
        Class<? extends HttpMessageConverter<?>> aClass,
        ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse
    ) {
        String path = serverHttpRequest.getURI().getPath();
        if (
            path != null &&
            (path.contains("/doc.html") ||
                path.contains("/swagger-ui") ||
                path.contains("/v3/api-docs") ||
                path.contains("/webjars"))
        ) {
            return body;
        }

        if (body == null) {
            if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                return ApiResult.success(body);
            } else {
                if ("text".equals(mediaType.getType())) {
                    try {
                        return objectMapper.writeValueAsString(ApiResult.success(body));
                    } catch (Exception e) {
                        return ApiResult.success(body).toString();
                    }
                }
                return body;
            }
        }
        if (body instanceof Resource) {
            return body;
        }

        if (body instanceof ApiResult) {
            return body;
        }
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(ApiResult.success(body));
            } catch (Exception e) {
                return ApiResult.success(body).toString();
            }
        }
        return ApiResult.success(body);
    }
}
