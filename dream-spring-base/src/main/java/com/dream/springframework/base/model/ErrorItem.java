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

/**
 * Error item information when bulk data action executed
 *
 * @author DreamJM
 */
public class ErrorItem {

    /**
     * Identity of the error data
     */
    private String id;

    /**
     * {@link com.dream.springframework.base.exception.BaseErrorCode Error Code}
     */
    private int code;

    /**
     * Error message
     */
    private String msg;

    public ErrorItem(String id, int code, String msg) {
        this.id = id;
        this.code = code;
        this.msg = msg;
    }

    /**
     * @return identity of the error data
     */
    public String getId() {
        return id;
    }

    /**
     * @return {@link com.dream.springframework.base.exception.BaseErrorCode Error Code}
     */
    public int getCode() {
        return code;
    }

    /**
     * @return error message
     */
    public String getMsg() {
        return msg;
    }
}
