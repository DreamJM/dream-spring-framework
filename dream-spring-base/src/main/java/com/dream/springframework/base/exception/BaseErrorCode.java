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

/**
 * Error Code that will result in {@link Result#getCode()}.<br>
 * Codes are between 0 and 5999
 * <ul>
 * <li>0-999 are designed for Success or partial Success (1-899 are remained for user, others are reserved for framework)</li>
 * <li>1000-1999 are designed for Parameter error (1001-1899 are remained for user, others are reserved for framework)</li>
 * <li>2000-2999 are designed for Authentication error (2001-2899 are remained for user, others are reserved for framework)</li>
 * <li>3000-3999 are designed for Access or Authority Error (3001-3899 are remained for user, others are reserved for framework)</li>
 * <li>4000-4999 are designed for Service Error (4001-4899 are remained for user, others are reserved for framework)</li>
 * <li>5000-5999 are designed for System Error (5001-5899 are remained for user, others are reserved for framework)</li>
 * </ul>
 *
 * @author DreamJM
 */
public abstract class BaseErrorCode {

    /*============ Success or Partial Success ============*/

    /**
     * Access Success
     */
    public static final int SUCCESS = 0;

    /**
     * Requested data not modified
     */
    public static final int DATA_NOT_MODIFIED = 901;

    /**
     * Delete in bulk action partially succeed
     */
    public static final int DELETE_PART_ERROR = 902;

    /*============ Parameter Error: 1000-1999 ============*/

    /**
     * Common code for parameter error
     */
    public static final int PARAM_INVALID = 1000;

    /**
     * Required parameter missing
     */
    public static final int PARAM_MISSING = 1901;

    /*============ Authentication Error: 2000-2999 ============*/

    /**
     * Common code for authentication error
     */
    public static final int AUTH_FAILURE = 2000;

    /**
     * User not found
     */
    public static final int USER_NOT_FOUND = 2901;

    /**
     * Password error
     */
    public static final int PASSWORD_ERROR = 2902;

    /**
     * Auth parameter missing
     */
    public static final int AUTH_MISSING = 2903;

    /**
     * Auth parameter format error
     */
    public static final int AUTH_FORMAT_ERROR = 2904;

    /**
     * Auth expired
     */
    public static final int AUTH_EXPIRED = 2905;

    /**
     * Error code for reaching the limitation of concurrent login number
     */
    public static final int AUTH_CONFLICT = 2906;

    /**
     * Old password error
     */
    public static final int OLD_PWD_ERROR = 2907;

    /**
     * Common code for token parse error
     */
    public static final int TOKEN_CHECK_ERROR = 2908;

    /*============ Access Authority Error: 3000-3999 ============*/

    /**
     * Common code for access error
     */
    public static final int ACCESS_DENY = 3000;

    /**
     * Organization resources access error
     */
    public static final int ORG_ACCESS_FORBIDDEN = 3901;

    /**
     * Error code for reaching the limitation of concurrent access count
     */
    public static final int ACCESS_LIMIT = 3902;

    /*============ Service Error: 4000-4999 ============*/

    /**
     * Data not found
     */
    public static final int DATA_NOT_FOUND = 4000;

    /**
     * Data already exists duration creation
     */
    public static final int DATA_EXISTS = 4901;

    /*============ System Error: 5000-5999 ============*/

    /**
     * Common code for system internal error
     */
    public static final int SYS_ERROR = 5000;

}
