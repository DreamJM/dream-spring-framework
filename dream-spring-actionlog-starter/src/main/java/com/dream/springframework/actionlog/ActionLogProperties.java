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

import com.dream.springframework.actionlog.annotation.ActionLogHint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Locale;

/**
 * Action log properties
 *
 * @author DreamJM
 */
@ConfigurationProperties(prefix = "dream.actionlog")
public class ActionLogProperties {

    /**
     * i18n enabled for {@link ActionLogHint#name()}. {@link com.dream.springframework.base.util.MessageUtils} will be used for i18n
     *
     * @see com.dream.springframework.base.util.MessageUtils
     */
    private boolean i18nEnabled = false;

    /**
     * Locale for i18n
     * <p>
     * Uses the name of static fields in {@link Locale} (case ignored)
     */
    private String locale;

    private final TaskExecutor executor = new TaskExecutor();

    /**
     * @return whether i18n enabled for {@link ActionLogHint#name()}
     */
    public boolean isI18nEnabled() {
        return i18nEnabled;
    }

    /**
     * @param i18nEnabled true if i18n enabled for {@link ActionLogHint#name()}
     */
    public void setI18nEnabled(boolean i18nEnabled) {
        this.i18nEnabled = i18nEnabled;
    }

    /**
     * Locale for i18n.
     *
     * @return locale name for i18n
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Specifies locale for i18n. Uses the name of static fields in {@link Locale} (case ignored)
     *
     * @param locale locale name for i18n
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return Executor properties
     */
    public TaskExecutor getExecutor() {
        return executor;
    }

    /**
     * Action Log task executor configuration
     */
    public static class TaskExecutor {

        /**
         * Core thread size
         */
        private int coreSize = 3;

        /**
         * Max thread size
         */
        private int maxSize = 10;

        /**
         * Queue capacity
         */
        private int queueCapacity = 999;

        /**
         * Thread name prefix
         */
        private String threadNamePrefix = "Action-Log-Task-";

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
}
