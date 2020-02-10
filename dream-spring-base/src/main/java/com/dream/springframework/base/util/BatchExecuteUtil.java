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

package com.dream.springframework.base.util;

import com.dream.springframework.base.exception.BaseErrorCode;
import com.dream.springframework.base.exception.RequestException;
import com.dream.springframework.base.model.ErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Batch action execute utilities
 *
 * @author DreamJM
 */
public class BatchExecuteUtil {

    private static final Logger logger = LoggerFactory.getLogger(BatchExecuteUtil.class);

    /**
     * Applies each value to the executor and collect execution errors to return
     *
     * @param values   values to be executed on
     * @param executor value action executor
     * @param <T>      value type
     * @return execution errors
     */
    public static <T> List<ErrorItem> executeBatch(List<T> values, ExConsumer<T, RequestException> executor) {
        List<ErrorItem> errors = new ArrayList<>();
        if (values != null) {
            for (T value : values) {
                try {
                    executor.accept(value);
                } catch (RequestException ex) {
                    errors.add(new ErrorItem(String.valueOf(value), ex.getCode(), ex.getMessage()));
                } catch (Exception e) {
                    errors.add(
                            new ErrorItem(String.valueOf(value), BaseErrorCode.SYS_ERROR, MessageUtils.getError(BaseErrorCode.SYS_ERROR)));
                    logger.error("Batch execution error!", e);
                }
            }
        }
        return errors;
    }

}
