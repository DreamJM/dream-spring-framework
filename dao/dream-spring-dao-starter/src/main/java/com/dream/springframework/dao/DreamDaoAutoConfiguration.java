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

package com.dream.springframework.dao;

import com.dream.springframework.dao.exception.DbExceptionHandlerAdvice;
import com.dream.springframework.dao.query.QueryConditionAspect;
import com.dream.springframework.dao.util.PageUtil;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import com.github.pagehelper.autoconfigure.PageHelperProperties;
import com.google.common.base.Strings;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dream sql dao framework auto configuration
 *
 * @author DreamJM
 */
@Configuration
@AutoConfigureBefore(PageHelperAutoConfiguration.class)
@EnableConfigurationProperties(DreamDaoProperties.class)
public class DreamDaoAutoConfiguration {

    public DreamDaoAutoConfiguration(DreamDaoProperties properties, PageHelperProperties phProperties) {
        PageUtil.setDefaultPageSize(properties.getDefaultPageSize());
        if (Strings.isNullOrEmpty(phProperties.getReasonable())) {
            // makes default page helper reasonable if not set
            phProperties.setReasonable(String.valueOf(true));
        }
    }

    /**
     * Query condition aspect to apply paging or ordering parameters using {@link com.github.pagehelper.PageHelper}
     *
     * <p>Supports {@link com.dream.springframework.dao.annotation.QueryCondition}<br>
     *
     * @return query condition aspect
     */
    @Bean
    @ConditionalOnClass(Aspect.class)
    public QueryConditionAspect queryConditionAspect() {
        return new QueryConditionAspect();
    }

    /**
     * @return database global exception handler
     */
    @Bean
    public DbExceptionHandlerAdvice dbExceptionHandlerAdvice() {
        return new DbExceptionHandlerAdvice();
    }

}
