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

package com.dream.springframework.dao.query;

import com.dream.springframework.dao.annotation.QueryCondition;
import com.dream.springframework.dao.model.BaseCondition;
import com.dream.springframework.dao.util.DbQueryUtils;
import com.github.pagehelper.Page;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Query condition aspect to apply paging or ordering parameters using {@link com.github.pagehelper.PageHelper}
 *
 * @author DreamJM
 * @see QueryCondition
 */
@Aspect
public class QueryConditionAspect {

    @Pointcut("@annotation(queryCondition)")
    public void applyQueryCondition(QueryCondition queryCondition) {
    }

    @Around(value = "applyQueryCondition(queryCondition)", argNames = "joinPoint,queryCondition")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, QueryCondition queryCondition) throws Throwable {
        BaseCondition condition = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseCondition) {
                condition = (BaseCondition) arg;
                break;
            }
        }
        if (condition != null) {
            return DbQueryUtils.applyQueryConditionThrowable(condition, appliedCondition -> (Page<?>) joinPoint.proceed());
        }
        return joinPoint.proceed();
    }

}
