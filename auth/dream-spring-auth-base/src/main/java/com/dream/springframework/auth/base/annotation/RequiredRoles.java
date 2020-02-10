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

package com.dream.springframework.auth.base.annotation;

import com.dream.springframework.auth.base.BaseAuthUser;
import com.dream.springframework.auth.base.component.DefaultAuthorizationProvider;
import com.dream.springframework.auth.base.service.AuthorizationProvider;

import java.lang.annotation.*;

/**
 * Defines required roles for the target request api
 * <ul>
 * <li>If no {@link OrgAuthorization} annotated parameter found, then the <em>common resource authorized roles</em> will be used to check with</li>
 * <li>If {@link OrgAuthorization} annotated parameter found, then the <em>organization based authorized roles</em> will be used to check with</li>
 * <li>Authorized <em>roles</em> are provided by {@link AuthorizationProvider#getRoles(String, BaseAuthUser)},
 * and the default implementation is {@link DefaultAuthorizationProvider#getAuthorities(String, BaseAuthUser)}.</li>
 * </ul>
 *
 * @author JiangMeng
 * @version 1.0，2019/7/18
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredRoles {

    /**
     * @return authorized roles
     */
    String[] value();

    /**
     * @return combination logic
     */
    Logical logical() default Logical.OR;
}
