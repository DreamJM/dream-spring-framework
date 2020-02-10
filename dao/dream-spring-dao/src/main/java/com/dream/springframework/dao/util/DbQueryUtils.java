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

package com.dream.springframework.dao.util;

import com.dream.springframework.base.util.ExFunction;
import com.dream.springframework.dao.model.BaseCondition;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;

import java.util.function.Function;

/**
 * Database Utilities
 * <p>
 * Includes paging and ordering function
 *
 * @author DreamJM
 */
public class DbQueryUtils {

    /**
     * Apply query condition to query function
     *
     * @param condition Query Condition(paging and ordering)
     * @param queryFunc Query function
     * @param <T>       Query condition type
     * @param <R>       Result Item type
     * @return Page of result
     */
    public static <T extends BaseCondition, R> Page<R> applyQueryCondition(T condition, Function<T, Page<R>> queryFunc) {
        if (condition.skipQuery()) {
            return new Page<>();
        }
        boolean needClear = applyCondition(condition);
        try {
            return queryFunc.apply(condition);
        } finally {
            if (needClear) {
                PageHelper.clearPage();
            }
        }
    }

    /**
     * Apply query condition to query function with specified exception
     *
     * @param condition Query Condition(paging and ordering)
     * @param queryFunc Query function
     * @param <T>       Query condition type
     * @param <R>       Result Item type
     * @param <E>       Specified exception when handling
     * @return Page of result
     */
    public static <T extends BaseCondition, R, E extends Throwable> Page<R> applyQueryConditionEx(T condition,
                                                                                                  ExFunction<T, Page<R>, E> queryFunc)
            throws E {
        if (condition.skipQuery()) {
            return new Page<>();
        }
        boolean needClear = applyCondition(condition);
        try {
            return queryFunc.apply(condition);
        } finally {
            if (needClear) {
                PageHelper.clearPage();
            }
        }
    }

    /**
     * Apply query condition to query function with common exception
     *
     * @param condition Query Condition(paging and ordering)
     * @param queryFunc Query function
     * @param <T>       Query condition type
     * @param <R>       Result Item type
     * @return Page of result
     */
    public static <T extends BaseCondition, R> Page<R> applyQueryConditionThrowable(T condition,
                                                                                    ExFunction<T, Page<R>, Throwable> queryFunc)
            throws Throwable {
        return applyQueryConditionEx(condition, queryFunc);
    }

    /**
     * @param condition Query condition object
     * @return whether paging or ordering condition applied using {@link PageHelper}
     */
    private static boolean applyCondition(BaseCondition condition) {
        boolean applied = PageUtil.startPage(condition.getPageNum(), condition.getPageSize());
        if (!Strings.isNullOrEmpty(condition.getOrder())) {
            PageHelper.orderBy(condition.getOrder());
            return true;
        }
        return applied;
    }
}
