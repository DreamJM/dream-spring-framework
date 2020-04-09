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

package com.dream.springframework.auth.token;

import com.dream.springframework.auth.base.DreamAuthProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Token based authorization properties
 *
 * @author DreamJM
 * @see DreamAuthProperties
 */
@ConfigurationProperties(prefix = "dream.auth")
public class DreamTokenHeaderAuthProperties extends DreamAuthProperties {

    private String authHeader = "X-Dream-Token";

    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }
}
