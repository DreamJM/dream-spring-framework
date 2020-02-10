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
import com.dream.springframework.auth.base.component.DefaultAuthorizationProvider;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * Provides user authorities and roles
 * <p>
 * {@link DefaultAuthorizationProvider} is provided as default provider.
 * But under certain circumstances(for example: organization has children and the authorities and roles may take effects automatically in
 * their children), you may want to customize the provider's behavior by implementing this interface and injecting it into spring context.
 *
 * @author DreamJM
 */
public interface AuthorizationProvider {

    /**
     * Gets user's authorities by the organization identity.
     * If 'orgId' is null, authorities for common resource should be provided.
     *
     * @param orgId        target organization identity
     * @param baseAuthUser authenticated user information
     * @return authorized authorities
     */
    @NonNull
    Set<String> getAuthorities(@Nullable String orgId, BaseAuthUser baseAuthUser);

    /**
     * Gets user's roles by the organization identity.
     * If 'orgId' is null, roles for common resource should be provided.
     *
     * @param orgId        target organization identity
     * @param baseAuthUser authenticated user information
     * @return authorized roles
     */
    @NonNull
    Set<String> getRoles(@Nullable String orgId, BaseAuthUser baseAuthUser);
}
