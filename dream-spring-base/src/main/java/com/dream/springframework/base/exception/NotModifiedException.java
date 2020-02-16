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

import org.springframework.http.HttpStatus;

/**
 * Data not modified exception with http response code 304
 *
 * @author DreamJM
 * @see RequestException
 */
public class NotModifiedException extends RequestException {

    public NotModifiedException(int code) {
        super(code);
    }

    public NotModifiedException(int code, Object[] params) {
        super(code, params);
    }

    public NotModifiedException(int code, String msg) {
        super(code, msg);
    }

    public NotModifiedException(int code, Throwable cause) {
        super(code, cause);
    }

    public NotModifiedException(int code, Throwable cause, Object[] params) {
        super(code, cause, params);
    }

    public NotModifiedException(int code, String msg, Throwable throwable) {
        super(code, msg, throwable);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_MODIFIED;
    }
}
