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

/**
 * Token based authentication user information which conveyed in customized HTTP Header
 *
 * @author DreamJM
 */
public class TokenHeaderAuthUser extends TokenAuthUser {

    /**
     * Whether token is needed to refresh
     */
    private boolean needRefresh;

    public TokenHeaderAuthUser(String uid, String token) {
        super(uid, token);
    }

    /**
     * @return whether to refresh token
     */
    public boolean isNeedRefresh() {
        return needRefresh;
    }

    /**
     * @param needRefresh true if token needed to refresh
     */
    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }
}
