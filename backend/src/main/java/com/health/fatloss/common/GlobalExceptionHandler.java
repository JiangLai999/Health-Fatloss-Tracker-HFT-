package com.health.fatloss.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统异常，请联系管理员");
    }

    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(error -> sb.append(error.getDefaultMessage()).append("; "));
        return Result.error(sb.toString());
    }

    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(error -> sb.append(error.getDefaultMessage()).append("; "));
        return Result.error(sb.toString());
    }
}
