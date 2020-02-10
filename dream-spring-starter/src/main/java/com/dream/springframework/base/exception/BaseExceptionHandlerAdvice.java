/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dream.springframework.base.exception;

import com.dream.springframework.base.model.Result;
import com.dream.springframework.base.util.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Global Exception handler
 *
 * @author DreamJM
 */
@ControllerAdvice
@ResponseBody
public class BaseExceptionHandlerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(BaseExceptionHandlerAdvice.class);

    @ExceptionHandler(RequestException.class)
    public Result handleRequestException(RequestException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatus().value());
        logger.warn("Error Returned with Code {} and Message {}", ex.getCode(), ex.getMessage());
        return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(RuntimeRequestException.class)
    public Result handleRuntimeRequestException(RuntimeRequestException ex, HttpServletResponse response) {
        return handleRequestException((RequestException) ex.getCause(), response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleBindException(MethodArgumentNotValidException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        FieldError error = ex.getBindingResult().getFieldError();
        String errorMsg = null;
        if (error != null && error.getDefaultMessage() != null) {
            errorMsg = MessageUtils.get(error.getDefaultMessage());
        }
        return new Result(BaseErrorCode.PARAM_INVALID, errorMsg);
    }

    @ExceptionHandler(InternalServerException.class)
    public Result handleServerException(InternalServerException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatus().value());
        logger.warn("Internal Server Returned with Code {} and Message {}", ex.getCode(), ex.getMessage());
        return new Result(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("Internal Server Error", ex);
        return new Result(BaseErrorCode.SYS_ERROR, MessageUtils.getError(BaseErrorCode.SYS_ERROR));
    }
}
