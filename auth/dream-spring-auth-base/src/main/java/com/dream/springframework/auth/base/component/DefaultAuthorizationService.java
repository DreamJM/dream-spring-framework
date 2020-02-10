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

package com.dream.springframework.auth.base.component;

import com.dream.springframework.auth.base.BaseAuthUser;
import com.dream.springframework.auth.base.DreamAuthProperties;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.auth.base.service.AuthorizationProvider;
import com.dream.springframework.auth.base.service.AuthorizationService;
import com.dream.springframework.base.exception.BaseErrorCode;
import com.dream.springframework.base.exception.ForbiddenException;
import com.dream.springframework.base.exception.RequestException;

import java.util.Arrays;
import java.util.Set;

/**
 * Default authorization service for required authorities and roles.
 *
 * @author DreamJM
 * @see RequiredAuthorities
 * @see RequiredRoles
 */
public class DefaultAuthorizationService implements AuthorizationService {

    private DreamAuthProperties properties;

    private AuthorizationProvider provider;

    public DefaultAuthorizationService(DreamAuthProperties properties, AuthorizationProvider provider) {
        this.properties = properties;
        this.provider = provider;
    }

    @Override
    public void authorize(BaseAuthUser authUser, String orgId, RequiredAuthorities authAnnotation, RequiredRoles roleAnnotation)
            throws RequestException {
        if (authAnnotation == null && roleAnnotation == null) {
            return;
        }
        boolean skipAuth = false;
        Set<String> roleIdSet = null;
        if (properties.getSkipAuthRoleIds() != null && !properties.getSkipAuthRoleIds().isEmpty()) {
            roleIdSet = provider.getRoles(orgId, authUser);
            for (String skipRoleId : properties.getSkipAuthRoleIds()) {
                if (roleIdSet.contains(skipRoleId)) {
                    skipAuth = true;
                    break;
                }
            }
        }
        skipAuth = skipAuth || (properties.getSkipAuthUids() != null && properties.getSkipAuthUids().contains(authUser.getUid()));
        if (!skipAuth) {
            checkAuthorities(authAnnotation, provider.getAuthorities(orgId, authUser));
            checkRoles(roleAnnotation, roleIdSet == null ? provider.getRoles(orgId, authUser) : roleIdSet);
        }
    }


    private void checkAuthorities(RequiredAuthorities authAnnotation, Set<String> authorities) throws RequestException {
        if (authAnnotation == null) {
            return;
        }
        if (authorities == null || !Arrays.stream(authAnnotation.value()).map(authorities::contains)
                .reduce(authAnnotation.logical().judge()).orElse(false)) {
            throw new ForbiddenException(BaseErrorCode.ACCESS_DENY);
        }
    }

    private void checkRoles(RequiredRoles roleAnnotation, Set<String> roles) throws RequestException {
        if (roleAnnotation == null) {
            return;
        }
        if (roles == null || !Arrays.stream(roleAnnotation.value()).map(roles::contains)
                .reduce(roleAnnotation.logical().judge()).orElse(false)) {
            throw new ForbiddenException(BaseErrorCode.ACCESS_DENY);
        }
    }
}
