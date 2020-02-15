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

package com.dream.springframework.demo.constant;

import com.dream.springframework.base.exception.BaseErrorCode;
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
public class ErrorCode extends BaseErrorCode {
}
