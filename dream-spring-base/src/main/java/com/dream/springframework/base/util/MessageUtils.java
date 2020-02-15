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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Message utilities for i18n and error code translation
 *
 * @author DreamJM
 */
public class MessageUtils {

    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    private static final String CODE_PREFIX = "code.";

    private static MessageSource messageSource;

    private static Locale locale;

    /**
     * Initializes with spring Message Source for i18n
     *
     * @param messageSource Message Source for i18n
     */
    public static void setMessageSource(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * Sets the locale forcibly for message.
     * <p>If not set or set null, {@link LocaleContextHolder#getLocale()} will be used for i18n
     *N
     * @param locale locale of i18n
     */
    public static void setLocale(Locale locale) {
        MessageUtils.locale = locale;
    }

    /**
     * Do i18n for the message key with parameters
     *
     * @param msgKey message key defined in specified i18n properties file
     * @param params parameters to replace in order
     * @return i18n result message
     */
    public static String get(String msgKey, Object... params) {
        try {
            return messageSource.getMessage(msgKey, params, locale == null ? LocaleContextHolder.getLocale() : locale);
        } catch (NoSuchMessageException ex) {
            logger.error("MsgKey " + msgKey + " missing", ex);
            return null;
        }
    }

    /**
     * Do i18n for the message key using specified locale with parameters
     *
     * @param msgKey message key defined in specified i18n properties file
     * @param locale specified locale
     * @param params parameters to replace in order
     * @return i18n result message
     */
    public static String getWithLocale(String msgKey, Locale locale, Object... params) {
        try {
            return messageSource.getMessage(msgKey, params, locale == null ? Locale.getDefault() : locale);
        } catch (NoSuchMessageException ex) {
            logger.error("MsgKey " + msgKey + " missing", ex);
            return null;
        }
    }

    /**
     * Gets the i18n error message for the specified error code.
     * <p>
     * Code specified in i18n properties files should start with 'code.'<br>
     * eg: code.4000=Data Not Found
     *
     * @param code   Error code
     * @param params Template parameters
     * @return error message
     */
    public static String getError(int code, Object... params) {
        return get(CODE_PREFIX + code, params);
    }
}
