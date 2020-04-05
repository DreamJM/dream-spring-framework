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

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Locale;

/**
 * Dream Spring Framework basic properties
 *
 * @author JiangMeng
 */
@ConfigurationProperties(prefix = "dream.framework")
public class DreamFrameworkProperties {

    /**
     * Enable {@link com.dream.springframework.base.annotation.AccessLimit @AccessLimit} support
     */
    private boolean accessLimitEnabled = false;

    /**
     * Async task executor properties
     */
    private final TaskExecutor executor = new TaskExecutor();

    /**
     * Locale and I18n properties
     */
    private final I18n i18n = new I18n();

    /**
     * @return whether {@link com.dream.springframework.base.annotation.AccessLimit @AccessLimit} is supported
     */
    public boolean isAccessLimitEnabled() {
        return accessLimitEnabled;
    }

    /**
     * @param accessLimitEnabled It {@code true}, enable the support for  {@link com.dream.springframework.base.annotation.AccessLimit @AccessLimit}
     */
    public void setAccessLimitEnabled(boolean accessLimitEnabled) {
        this.accessLimitEnabled = accessLimitEnabled;
    }

    /**
     * @return async task executor properties
     */
    public TaskExecutor getExecutor() {
        return this.executor;
    }

    /**
     * @return locale and i18n properties
     */
    public I18n getI18n() {
        return i18n;
    }

    /**
     * Async task executor properties
     */
    public static class TaskExecutor {

        /**
         * Core thread size
         */
        private int coreSize = 10;

        /**
         * Max thread size
         */
        private int maxSize = 100;

        /**
         * Queue capacity
         */
        private int queueCapacity = 9999;

        /**
         * Thread name prefix
         */
        private String threadNamePrefix = "Dream-Task-";

        /**
         * Whether allow the core threads timeout
         */
        private boolean allowCoreThreadTimeout = false;

        /**
         * Policy when queue reached max size
         * AbortPolicy: raise RejectedExecutionException
         * CallerRunsPolicy: execute on caller thread
         * DiscardPolicy: discard directly
         * DiscardOldestPolicy: discard the oldest task in queue
         */
        private String rejectExecutionHandler = "CallerRunsPolicy";

        /**
         * Max idle time for thread to timeout
         */
        private Duration keepAlive = Duration.ofSeconds(60);

        /**
         * @return core thread size
         */
        public int getCoreSize() {
            return coreSize;
        }

        /**
         * @param coreSize core thread size to set
         */
        public void setCoreSize(int coreSize) {
            this.coreSize = coreSize;
        }

        /**
         * @return Max thread size
         */
        public int getMaxSize() {
            return maxSize;
        }

        /**
         * @param maxSize Max thread size to set
         */
        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        /**
         * @return Queue capacity
         */
        public int getQueueCapacity() {
            return queueCapacity;
        }

        /**
         * @param queueCapacity Queue capacity to set
         */
        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        /**
         * @return thread name prefix
         */
        public String getThreadNamePrefix() {
            return threadNamePrefix;
        }

        /**
         * @param threadNamePrefix prefix for thread name
         */
        public void setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        /**
         * @return Whether allow the core threads timeout
         */
        public boolean isAllowCoreThreadTimeout() {
            return allowCoreThreadTimeout;
        }

        /**
         * @param allowCoreThreadTimeout {@code true} if core thread can be recycled when timeout
         */
        public void setAllowCoreThreadTimeout(boolean allowCoreThreadTimeout) {
            this.allowCoreThreadTimeout = allowCoreThreadTimeout;
        }

        /**
         * @return Policy when queue reached max size
         */
        public String getRejectExecutionHandler() {
            return rejectExecutionHandler;
        }

        /**
         * Policy when queue reached max size
         * AbortPolicy: raise RejectedExecutionException
         * CallerRunsPolicy: execute on caller thread
         * DiscardPolicy: discard directly
         * DiscardOldestPolicy: discard the oldest task in queue
         *
         * @param rejectExecutionHandler Policy when queue reached max size
         */
        public void setRejectExecutionHandler(String rejectExecutionHandler) {
            this.rejectExecutionHandler = rejectExecutionHandler;
        }

        /**
         * @return Max idle time for thread to timeout
         */
        public Duration getKeepAlive() {
            return keepAlive;
        }

        /**
         * @param keepAlive Max idle time for thread before timeout to set
         */
        public void setKeepAlive(Duration keepAlive) {
            this.keepAlive = keepAlive;
        }
    }

    /**
     * Swagger configuration properties
     */
    public static class Swagger {

        /**
         * Swagger title
         */
        private String title = "";

        /**
         * Swagger API version
         */
        private String version = "1.0";

        /**
         * Swagger annotation scan package
         */
        private String basePackage = "com.dream";

        /**
         * Ignored class types that will ignored in Swagger API
         */
        private Class<?>[] ignoredParameterTypes;

        /**
         * Whether Bearer Token is enabled
         */
        private boolean bearerTokenEnabled = true;

        /**
         * @return Swagger title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title Swagger title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return version of Swagger API document
         */
        public String getVersion() {
            return version;
        }

        /**
         * @param version version of Swagger API document to set
         */
        public void setVersion(String version) {
            this.version = version;
        }

        /**
         * @return base package for swagger annotation scan
         */
        public String getBasePackage() {
            return basePackage;
        }

        /**
         * @param basePackage base package for swagger annotation scan
         */
        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }

        /**
         * @return Ignored class types that will ignored in Swagger API
         */
        public Class<?>[] getIgnoredParameterTypes() {
            return ignoredParameterTypes;
        }

        /**
         * @param ignoredParameterTypes ignored class types that will ignored in Swagger API
         */
        public void setIgnoredParameterTypes(Class<?>[] ignoredParameterTypes) {
            this.ignoredParameterTypes = ignoredParameterTypes;
        }

        /**
         * @return Whether Bearer Token is enabled
         */
        public boolean isBearerTokenEnabled() {
            return bearerTokenEnabled;
        }

        /**
         * If true, Swagger api will add Authorization HTTP Header description
         *
         * @param bearerTokenEnabled {@code true} to enable the Bearer Token description
         */
        public void setBearerTokenEnabled(boolean bearerTokenEnabled) {
            this.bearerTokenEnabled = bearerTokenEnabled;
        }
    }

    /**
     * Locale and I18n properties
     */
    public static class I18n {

        /**
         * Default locale for i18n
         * <p>
         * Uses the name of static fields in {@link Locale} (case ignored)
         */
        private String defaultLocale;

        /**
         * Whether i18n is auto switchable (Using Accept-Language header or other method)
         */
        private boolean localeSwitch = true;

        /**
         * Locale resolver
         * <p>
         * default: Use HTTP Accept-Language header to define locale<br>
         * session: Use 'lang' request parameter to initialize session locale<br>
         * cookie: Use 'lang' request parameter to initialize locale(store in cookie)
         */
        private LocaleResolverType localeResolver = LocaleResolverType.DEFAULT;

        /**
         * Max age for locale value stored in cookie
         * <p>
         * Valid when {@link #localeResolver} is {@link LocaleResolverType#DEFAULT}
         */
        private Duration cookieMaxAge = Duration.ofDays(30);

        /**
         * @return default locale name for i18n
         */
        public String getDefaultLocale() {
            return defaultLocale;
        }

        /**
         * @param defaultLocale Default locale for i18n to set (Uses the name of static fields in {@link Locale} (case ignored))
         */
        public void setDefaultLocale(String defaultLocale) {
            this.defaultLocale = defaultLocale;
        }

        /**
         * @return whether i18n is auto switchable (Using Accept-Language header or other method)
         */
        public boolean isLocaleSwitch() {
            return localeSwitch;
        }

        /**
         * If false, {@link #defaultLocale} will be used for the server locale
         *
         * @param localeSwitch {@code true} to enable i18n auto-switch
         */
        public void setLocaleSwitch(boolean localeSwitch) {
            this.localeSwitch = localeSwitch;
        }

        /**
         * @return Locale Resolver Type
         */
        public LocaleResolverType getLocaleResolver() {
            return localeResolver;
        }

        /**
         * default: Use HTTP Accept-Language header to define locale<br>
         * session: Use 'lang' request parameter to initialize session locale<br>
         * cookie: Use 'lang' request parameter to initialize locale(store in cookie)
         *
         * @param localeResolver Locale Resolver Type to set
         */
        public void setLocaleResolver(LocaleResolverType localeResolver) {
            this.localeResolver = localeResolver;
        }

        /**
         * @return Max age for locale value stored in cookie
         */
        public Duration getCookieMaxAge() {
            return cookieMaxAge;
        }

        /**
         * Set the max age for locale value stored in cookie
         * <p>
         * Valid when {@link #localeResolver} is {@link LocaleResolverType#DEFAULT}
         *
         * @param cookieMaxAge max age for locale value stored in cookie
         */
        public void setCookieMaxAge(Duration cookieMaxAge) {
            this.cookieMaxAge = cookieMaxAge;
        }
    }

    public enum LocaleResolverType {
        /**
         * Use HTTP Accept-Language header to define locale
         */
        DEFAULT,
        /**
         * Use 'lang' request parameter to initialize session locale
         */
        SESSION,
        /**
         * Use 'lang' request parameter to initialize locale(store in cookie)
         */
        COOKIE,
        /**
         * Use 'lang' query parameter to initialize locale
         */
        QUERY
    }

}
