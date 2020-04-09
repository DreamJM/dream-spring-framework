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

package com.dream.springframework.auth.token.model;

import com.dream.springframework.auth.base.BaseAuthUser;

/**
 * Base class for token based authentication user information
 *
 * @author DreamJM
 */
public class TokenAuthUser extends BaseAuthUser {

    /**
     * issued user token
     */
    private String token;

    public TokenAuthUser(String uid, String token) {
        super(uid);
        this.token = token;
    }

    /**
     * @return issued user token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set token for user
     *
     * @param token token for user
     */
    public void setToken(String token) {
        this.token = token;
    }
}
