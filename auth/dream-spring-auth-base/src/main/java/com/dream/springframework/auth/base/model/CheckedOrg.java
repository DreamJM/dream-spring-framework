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

package com.dream.springframework.auth.base.model;

import com.dream.springframework.auth.base.service.OrgPermissionService;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Provides the organization identity to be checked for authorities, roles or resource access permissions.
 * <p>
 * Request parameter annotated with {@link org.springframework.web.bind.annotation.RequestBody} which willing to be checked for authorities,
 * roles or resource access permissions should implement this interface.
 * Example:
 * <pre class='code'>
 * {@code @PostMapping}
 * public void createDemo(@RequestBody Demo demo) throws RequestException {
 *     .......
 * }
 * </pre>
 * Above 'Demo' class implementing this interface will be checked automatically.
 * <ul>
 * <li>By default, the organization resource access permission will be checked with {@link OrgPermissionService}</li>
 * <li>If {@link com.dream.springframework.auth.base.annotation.RequiredAuthorities} is annotated on the method, and
 * {@link com.dream.springframework.auth.base.annotation.OrgAuthorization} is annotated with
 * {@link org.springframework.web.bind.annotation.RequestBody}, then organization based authorities will be checked with
 * {@link com.dream.springframework.auth.base.service.AuthorizationService}</li>
 * <li>If {@link com.dream.springframework.auth.base.annotation.RequiredRoles} is annotated on the method, and
 * {@link com.dream.springframework.auth.base.annotation.OrgAuthorization} is annotated with
 * {@link org.springframework.web.bind.annotation.RequestBody}, then organization based roles will be checked with
 * {@link com.dream.springframework.auth.base.service.AuthorizationService}</li>
 * </ul>
 *
 * @param <T> check passed organization model type(should conform to the generic type defined in {@link OrgPermissionService})
 * @author DreamJM
 */
public interface CheckedOrg<T> {

    /**
     * Provides organization identity
     *
     * @return identity of the organization that to be checked
     */
    @JsonIgnore
    String getCheckOrgId();

    /**
     * Set detail organization object
     * <p>
     * This method will be called automatically after resource permission checking passes
     *
     * @param checkedOrg detail organization object to set
     */
    void setCheckedOrg(T checkedOrg);

}
