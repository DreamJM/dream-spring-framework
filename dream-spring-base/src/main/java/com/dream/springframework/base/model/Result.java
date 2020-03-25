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

package com.dream.springframework.base.model;

import com.dream.springframework.base.util.MessageUtils;

/**
 * Wrapper for result data. Including {@link com.dream.springframework.base.exception.BaseErrorCode error code} and error message fields
 *
 * @param <T> result data type
 * @author DreamJM
 */
public class Result<T> {

    /**
     * {@link com.dream.springframework.base.exception.BaseErrorCode Error Code}
     */
    private int code;

    /**
     * Error message when {@link #code code} isn't zero
     */
    private String msg;

    /**
     * Result data
     */
    private T data;

    public Result(T data) {
        this.data = data;
    }

    public Result(int code) {
        this(code, MessageUtils.getError(code));
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @return {@link com.dream.springframework.base.exception.BaseErrorCode Error Code}
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets error code and complete error message automatically
     *
     * @param code {@link com.dream.springframework.base.exception.BaseErrorCode Error Code} to set
     */
    public void setCode(int code) {
        this.code = code;
        this.msg = MessageUtils.getError(code);
    }

    /**
     * If {@link #getCode() code} isn't zero, the message automatically assigned according to the Error Code
     *
     * @return Error Message
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @return Result data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data Result data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}
