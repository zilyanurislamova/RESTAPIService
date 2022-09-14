package com.example.service.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

@ControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseStatusException && ((ResponseStatusException) body).getStatus() == HttpStatus.NOT_FOUND) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return new Error(404, Error.ITEM_NOT_FOUND);
        }
        else if (body instanceof Throwable)
            return new Error(400, Error.VALIDATION_FAILED);
        else if (body instanceof LinkedHashMap && ((LinkedHashMap<?, ?>) body).containsKey("error")) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return new Error(400, Error.VALIDATION_FAILED);
        }
        return body;
    }
}
