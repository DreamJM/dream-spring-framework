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

package com.dream.springframework.base.component;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Locale resolver for query parameter with name 'lang'.
 * <p>
 * Using the value of 'lang' query parameter as the locale of the request. If no 'lang' found,
 * Accept-Language will be used for the request.
 *
 * @author DreamJM
 */
public class QueryLocaleResolver implements LocaleResolver {

    private Locale defaultLocale;

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String locale = request.getParameter("lang");
        if (StringUtils.isEmpty(locale)) {
            return defaultLocale == null ? request.getLocale() : defaultLocale;
        } else {
            String[] split = locale.split("_");
            return split.length > 1 ? new Locale(split[0], split[1]) : new Locale(split[0]);
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException(
                "Cannot change HTTP query url - use a different locale resolution strategy");
    }
}
