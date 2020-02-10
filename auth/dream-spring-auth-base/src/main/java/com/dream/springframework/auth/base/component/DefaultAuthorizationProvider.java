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
import com.dream.springframework.auth.base.service.AuthorizationProvider;
import com.google.common.base.Strings;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Default provider for authorities and roles
 * <p>
 * This default provider will uses {@link BaseAuthUser#getOrgAuthMap()} and {@link BaseAuthUser#getOrgRoleMap()} to get the organization
 * based authorities and roles. If 'orgId' is null, then {@link BaseAuthUser#getAuthIds()} and {@link BaseAuthUser#getRoleIds()} will be
 * used for common authorized authorities and roles.
 * <p>
 * Under certain circumstances(for example: organization has children and the authorities and roles may take effects automatically in their
 * children), you may want to customize the provider's behavior by implementing {@link AuthorizationProvider} and injecting it into spring
 * context.
 *
 * @author DreamJM
 */
public class DefaultAuthorizationProvider implements AuthorizationProvider {

    @NonNull
    @Override
    public Set<String> getAuthorities(@Nullable String orgId, BaseAuthUser baseAuthUser) {
        Set<String> authorities = null;
        if (Strings.isNullOrEmpty(orgId)) {
            authorities = baseAuthUser.getAuthIds();
        } else if (baseAuthUser.getOrgAuthMap() != null && baseAuthUser.getOrgAuthMap().containsKey(orgId)) {
            authorities = baseAuthUser.getOrgAuthMap().get(orgId);
        }
        return authorities == null ? new HashSet<>() : authorities;
    }

    @NonNull
    @Override
    public Set<String> getRoles(@Nullable String orgId, BaseAuthUser baseAuthUser) {
        Set<String> roles = null;
        if (Strings.isNullOrEmpty(orgId)) {
            roles = baseAuthUser.getRoleIds();
        } else if (baseAuthUser.getOrgRoleMap() != null && baseAuthUser.getOrgRoleMap().containsKey(orgId)) {
            roles = baseAuthUser.getOrgRoleMap().get(orgId);
        }
        return roles == null ? new HashSet<>() : roles;
    }
}
