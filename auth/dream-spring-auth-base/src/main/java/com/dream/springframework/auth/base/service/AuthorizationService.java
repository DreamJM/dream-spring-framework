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

package com.dream.springframework.auth.base.service;

import com.dream.springframework.auth.base.BaseAuthUser;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.base.exception.RequestException;

/**
 * Authorization service for required authorities and roles.
 *
 * @author DreamJM
 * @see RequiredAuthorities
 * @see RequiredRoles
 * @see com.dream.springframework.auth.base.annotation.OrgAuthorization
 */
public interface AuthorizationService {

    /**
     * Authorizes user for the required roles or authorities.
     *
     * @param authUser       authenticated user
     * @param orgId          organization identity
     * @param authAnnotation required authorities
     * @param roleAnnotation required roles
     * @throws RequestException authorization failure
     */
    void authorize(BaseAuthUser authUser, String orgId, RequiredAuthorities authAnnotation, RequiredRoles roleAnnotation) throws
            RequestException;
}
