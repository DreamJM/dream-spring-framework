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

package com.dream.springframework.auth.base.resolver;

import com.dream.springframework.auth.base.BaseAuthUser;
import com.dream.springframework.auth.base.annotation.OrgAuthorization;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.auth.base.service.AuthorizationService;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

/**
 * Path variable resolver that contains authorization function based on {@link OrgAuthorization} annotated value
 *
 * @author DreamJM
 */
public class OrgAuthCheckPathVariableResolver extends PathVariableMethodArgumentResolver {

    private AuthorizationService service;

    public OrgAuthCheckPathVariableResolver(AuthorizationService service) {
        this.service = service;
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Object result = super.resolveName(name, parameter, request);
        OrgAuthorization orgAuthCheck = parameter.getParameterAnnotation(OrgAuthorization.class);
        if (orgAuthCheck != null && parameter.getMethod() != null) {
            BaseAuthUser authUser = (BaseAuthUser) request.getAttribute(BaseAuthUser.USER_KEY, RequestAttributes.SCOPE_REQUEST);
            if (authUser != null) {
                RequiredAuthorities authAnnotation = parameter.getMethodAnnotation(RequiredAuthorities.class);
                RequiredRoles roleAnnotation = parameter.getMethodAnnotation(RequiredRoles.class);
                service.authorize(authUser, result == null ? null : String.valueOf(result), authAnnotation, roleAnnotation);
            }
        }
        return result;
    }
}
