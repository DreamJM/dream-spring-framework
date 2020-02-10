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
 * Skip organization resource access permission checking even if {@link CheckedOrg}, {@link CheckedOrgList} or {@link CheckedOrgs} is
 * implements as request parameter
 *
 * @author DreamJM
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrgPermissionIgnore {
}
