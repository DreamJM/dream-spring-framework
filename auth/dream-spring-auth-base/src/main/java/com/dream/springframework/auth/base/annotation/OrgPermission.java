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

import com.dream.springframework.auth.base.model.CheckedOrg;
import com.dream.springframework.auth.base.model.CheckedOrgList;
import com.dream.springframework.auth.base.model.CheckedOrgs;

import java.lang.annotation.*;

/**
 * Annotate on {@link org.springframework.web.bind.annotation.PathVariable}, {@link org.springframework.web.bind.annotation.RequestParam},
 * {@link org.springframework.web.bind.annotation.RequestBody} annotated parameter or method which represents or contains organization
 * identity to check the resource permission.
 * <ul>
 * <li>{@link org.springframework.web.bind.annotation.PathVariable} or {@link org.springframework.web.bind.annotation.RequestParam}
 * annotated parameter that annotated with this annotation should represents the organization identity</li>
 * <li>{@link org.springframework.web.bind.annotation.RequestBody} annotated parameter that annotated with this annotation should
 * implement {@link CheckedOrg}, {@link CheckedOrgList} or {@link CheckedOrgs}</li>
 * <li>when annotated on the method, the response object will be used to check authorities or roles</li>
 * </ul>
 *
 * @author DreamJM
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrgPermission {

    /**
     * Whether to skip checking of the {@code null} organization or empty list.
     * <p>
     * If true, checking will be skipped while null or empty. Else {@link com.dream.springframework.base.exception.ForbiddenException} will
     * be thrown with code {@link com.dream.springframework.base.exception.BaseErrorCode#EMPTY_ORG_FORBIDDEN}
     *
     * @return Whether to skip checking of the {@code null} organization or empty list.
     */
    boolean skipEmpty() default false;
}
