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

package com.dream.springframework.demo.service;

import com.dream.springframework.auth.token.component.BaseTokenAuthenticationService;
import com.dream.springframework.auth.token.exception.TokenException;
import com.dream.springframework.auth.token.exception.TokenExpiredException;
import com.dream.springframework.auth.token.model.TokenAuthUser;
import com.dream.springframework.base.exception.RequestException;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

/**
 * @author DreamJM
 */
@Service
public class SessionService extends BaseTokenAuthenticationService<TokenAuthUser> {

    @Override
    protected TokenAuthUser parseToken(String token) throws TokenException, TokenExpiredException, RequestException {
        TokenAuthUser authUser = new TokenAuthUser("1", token);
        authUser.setAuthIds(Sets.newHashSet("auth1", "auth2", "auth5"));
        authUser.setOrgIds(Sets.newHashSet("org1", "org2"));
        authUser.setRoleIds(Sets.newHashSet("role1", "role2", "role5"));
        authUser.setOrgAuthMap(new HashMap<String, Set<String>>() {
            {
                put("org1", Sets.newHashSet("auth1", "auth2", "auth3"));
                put("org2", Sets.newHashSet("auth1", "auth2", "auth4"));
            }
        });
        authUser.setOrgRoleMap(new HashMap<String, Set<String>>() {
            {
                put("org1", Sets.newHashSet("role1", "role2", "role3"));
                put("org2", Sets.newHashSet("role1", "role2", "role4"));
            }
        });
        return authUser;
    }
}
