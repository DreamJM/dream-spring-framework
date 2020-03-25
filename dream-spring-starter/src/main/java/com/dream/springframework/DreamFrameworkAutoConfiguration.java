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

package com.dream.springframework;

import com.dream.springframework.base.access.AccessLimitAspect;
import com.dream.springframework.base.exception.BaseExceptionHandlerAdvice;
import com.dream.springframework.base.util.EventPubUtils;
import com.dream.springframework.base.util.JacksonUtils;
import com.dream.springframework.base.util.MessageUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * Dream spring framework starter for spring boot
 *
 * <ul>
 * <li>Uses customizable task executor instead of default {@link TaskExecutionAutoConfiguration#applicationTaskExecutor(TaskExecutorBuilder) application task executor}</li>
 * <li>Provides default jackson configuration</li>
 * <li>Initializes {@link MessageUtils} for i18n and {@link ApplicationEventPublisher} for global event publisher</li>
 * <li>Customizes Locale for i18n</li>
 * </ul>
 *
 * @author DreamJM
 */
@EnableAsync
@AutoConfigureBefore({TaskExecutionAutoConfiguration.class, JacksonAutoConfiguration.class})
@Configuration
@EnableConfigurationProperties(DreamFrameworkProperties.class)
public class DreamFrameworkAutoConfiguration implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DreamFrameworkAutoConfiguration.class);

    private DreamFrameworkProperties properties;

    public DreamFrameworkAutoConfiguration(DreamFrameworkProperties properties, MessageSource messageSource,
                                           ApplicationEventPublisher publisher) throws Exception {
        this.properties = properties;
        MessageUtils.setMessageSource(messageSource);
        if (!properties.getI18n().isLocaleSwitch()) {
            MessageUtils.setLocale(convertLocale(properties.getI18n().getDefaultLocale()));
        }
        EventPubUtils.setPublisher(publisher);
    }

    /**
     * Creates accept header locale resolver.
     *
     * <p>Uses Accept-Language HTTP header for i18n, if Accept-Language header not found,
     * then {@link DreamFrameworkProperties.I18n#getDefaultLocale()} will be used.
     * If {@link DreamFrameworkProperties.I18n#getDefaultLocale()} is null, then {@link Locale#getDefault()} will be used as default locale.
     *
     * @return accept header locale resolver
     * @throws IllegalAccessException Reflection Exception
     */
    @Bean("localeResolver")
    @ConditionalOnMissingBean(name = "localeResolver")
    @ConditionalOnExpression("'${dream.framework.i18n.locale-resolver:default}' == 'default'")
    public LocaleResolver defaultLocaleResolver() throws IllegalAccessException {
        AcceptHeaderLocaleResolver ahlr = new AcceptHeaderLocaleResolver();
        if (properties.getI18n().getDefaultLocale() != null) {
            ahlr.setDefaultLocale(convertLocale(properties.getI18n().getDefaultLocale()));
        }
        return ahlr;
    }

    /**
     * Creates cookie locale resolver.
     *
     * <p>Uses 'locale' cookie value as the locale. If 'locale' cookie not found, then
     * {@link DreamFrameworkProperties.I18n#getDefaultLocale()} will be used.
     * If {@link DreamFrameworkProperties.I18n#getDefaultLocale()} is null, then {@link Locale#getDefault()} will be used as default locale.
     *
     * @return cookie locale resolver
     * @throws IllegalAccessException Reflection Exception
     */
    @Bean("localeResolver")
    @ConditionalOnMissingBean(name = "localeResolver")
    @ConditionalOnProperty(prefix = "dream.framework.i18n", name = "locale-resolver", havingValue = "cookie")
    public LocaleResolver cookieLocaleResolver() throws IllegalAccessException {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setCookieName("locale");
        if (properties.getI18n().getDefaultLocale() != null) {
            clr.setDefaultLocale(convertLocale(properties.getI18n().getDefaultLocale()));
        }
        clr.setCookieMaxAge((int) properties.getI18n().getCookieMaxAge().getSeconds());
        return clr;
    }

    /**
     * Creates session locale resolver.
     *
     * <p>Uses session to store the locale. If locale not found in session, then
     * {@link DreamFrameworkProperties.I18n#getDefaultLocale()} will be used.
     * If {@link DreamFrameworkProperties.I18n#getDefaultLocale()} is null, then {@link Locale#getDefault()} will be used as default locale.
     *
     * @return cookie locale resolver
     * @throws IllegalAccessException Reflection Exception
     */
    @Bean("localeResolver")
    @ConditionalOnMissingBean(name = "localeResolver")
    @ConditionalOnProperty(prefix = "dream.framework.i18n", name = "locale-resolver", havingValue = "session")
    public LocaleResolver localeResolver() throws IllegalAccessException {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        if (properties.getI18n().getDefaultLocale() != null) {
            slr.setDefaultLocale(convertLocale(properties.getI18n().getDefaultLocale()));
        }
        return slr;
    }

    /**
     * Locale Change interceptor for {@link SessionLocaleResolver} and {@link CookieLocaleResolver}
     *
     * <p>Request parameter with name 'lang' will be used for the locale and stored in session or cookie
     *
     * @return Locale Change Interceptor
     */
    @Bean("localeChangeInterceptor")
    @ConditionalOnBean(name = "localeResolver")
    @ConditionalOnExpression("{'session','cookie'}.contains('${dream.framework.i18n.locale-resolver:default}')")
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    /**
     * Task Executor for @Async，instead of default spring application task executor
     *
     * @return Task Executor
     * @throws ClassNotFoundException Reflection Exception
     * @throws IllegalAccessException Reflection Exception
     * @throws InstantiationException Reflection Exception
     */
    @Bean
    @ConditionalOnMissingBean(name = "taskExecutor")
    public Executor taskExecutor() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.info("Initializing Dream Task Executor");
        DreamFrameworkProperties.TaskExecutor executorProperties = properties.getExecutor();
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
     * Provides and configures default jackson mapper
     *
     * @param builder jackson mapper builder
     * @return jackson mapper
     */
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        logger.info("Initializing Object Mapper");
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return objectMapper;
    }

    /**
     * Global Jackson Util
     *
     * @param mapper jackson object mapper
     * @return Jackson Global Util
     */
    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public JacksonUtils jacksonUtils(ObjectMapper mapper) {
        return new JacksonUtils(mapper);
    }

    /**
     * Creates concurrent access limitation aspect.
     *
     * <p>Supports {@link com.dream.springframework.base.annotation.AccessLimit}.<br>
     * Takes effect when 'dream.framework.access-limit-enabled' is set to true and 'spring-boot-starter-aop' is added to dependencies
     *
     * @return concurrent access limitation aspect
     */
    @Bean
    @ConditionalOnClass(Aspect.class)
    @ConditionalOnProperty(prefix = "dream.framework", name = "access-limit-enabled", havingValue = "true")
    public AccessLimitAspect accessLimitAspect() {
        return new AccessLimitAspect();
    }

    /**
     * @return Global Exception handler
     */
    @Bean
    public BaseExceptionHandlerAdvice baseExceptionHandlerAdvice() {
        return new BaseExceptionHandlerAdvice();
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
