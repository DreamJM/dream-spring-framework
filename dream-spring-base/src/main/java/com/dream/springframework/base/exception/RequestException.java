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

import com.dream.springframework.base.util.MessageUtils;
import org.springframework.http.HttpStatus;

/**
 * Common Request Exception with http response code 400
 *
 * @author DreamJM
 */
public class RequestException extends Exception {

    private int code;

    private Object data;

    public RequestException(int code) {
        this(code, MessageUtils.getError(code));
    }

    public RequestException(int code, Object[] params) {
        this(code, MessageUtils.getError(code, params));
    }

    public RequestException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public RequestException(int code, Throwable cause) {
        super(MessageUtils.getError(code), cause);
        this.code = code;
    }

    public RequestException(int code, Throwable cause, Object[] params) {
        super(MessageUtils.getError(code, params), cause);
        this.code = code;
    }

    public RequestException(int code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
