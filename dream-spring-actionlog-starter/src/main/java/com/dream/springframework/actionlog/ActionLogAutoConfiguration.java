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

package com.dream.springframework.actionlog;

import com.dream.springframework.actionlog.component.ActionLogAspect;
import com.dream.springframework.actionlog.repository.ActionLogCreationRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * Action log auto configuration.
 * <p>
 *
 *
 * @author DreamJM
 */
@ConditionalOnBean(ActionLogCreationRepo.class)
@Configuration
@EnableConfigurationProperties(ActionLogProperties.class)
public class ActionLogAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ActionLogAutoConfiguration.class);

    private ActionLogProperties properties;

    public ActionLogAutoConfiguration(ActionLogProperties properties) {
        this.properties = properties;
    }

    /**
     * Executor for action log serialization and storage
     *
     * @return Executor for action log serialization
     * @throws ClassNotFoundException Reflection Exception
     * @throws IllegalAccessException Reflection Exception
     * @throws InstantiationException Reflection Exception
     */
    @ConditionalOnMissingBean(name = "actionLogExecutor")
    @Bean
    public Executor actionLogExecutor() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("Initializing Caltta Action Log Task Executor");
        ActionLogProperties.TaskExecutor executorProperties = properties.getExecutor();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorProperties.getCoreSize());
        executor.setMaxPoolSize(executorProperties.getMaxSize());
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(executorProperties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(executorProperties.isAllowCoreThreadTimeout());
        executor.setKeepAliveSeconds((int) executorProperties.getKeepAlive().getSeconds());
        Class<?> clazz = Class.forName("java.util.concurrent.ThreadPoolExecutor$" + executorProperties.getRejectExecutionHandler());
        executor.setRejectedExecutionHandler((RejectedExecutionHandler) clazz.newInstance());
        executor.initialize();
        return executor;
    }

    /**
     * Aop aspect to collect, serialize and store action information
     *
     * @param actionLogCreationRepo repository to store action log information
     * @param actionLogExecutor     executor to store and serialize action log
     * @param mapper                jackson mapper for serialization
     * @return aop aspect to collect, serialize and store action information
     * @throws IllegalAccessException Reflection Error
     */
    @Bean
    public ActionLogAspect actionLogAspect(ActionLogCreationRepo actionLogCreationRepo, Executor actionLogExecutor, ObjectMapper mapper)
            throws IllegalAccessException {
        return new ActionLogAspect(actionLogCreationRepo, actionLogExecutor, mapper, properties.isI18nEnabled(),
                Strings.isNullOrEmpty(properties.getLocale()) ? null : convertLocale(properties.getLocale()));
    }

    /**
     * Gets Locale object with then name of static fields in {@link Locale} (case ignored)
     *
     * @param name name of static fields in {@link Locale} (case ignored)
     * @return Locale object for i18n
     * @throws IllegalAccessException Reflection Exception
     */
    private static Locale convertLocale(String name) throws IllegalAccessException {
        Field[] fields = Locale.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == Locale.class && Modifier.isStatic(field.getModifiers()) && field.getName().equalsIgnoreCase(name)) {
                return (Locale) field.get(Locale.class);
            }
        }
        throw new RuntimeException("Locale with name " + name + " not found!");
    }

}
