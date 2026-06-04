package com.anran.lims.web;

import com.anran.lims.common.exception.BusinessException;
import com.anran.lims.common.exception.ErrorCode;
import com.anran.lims.common.result.ApiResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        return ApiResponse.fail(ex.code(), ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<Void> handleValidationException(Exception ex) {
        return ApiResponse.fail(ErrorCode.VALIDATION_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex) {
        return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "Internal server error");
    }
}
