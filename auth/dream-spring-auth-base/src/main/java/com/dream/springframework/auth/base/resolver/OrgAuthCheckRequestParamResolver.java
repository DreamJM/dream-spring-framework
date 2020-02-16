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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import java.lang.reflect.Array;

/**
 * Request param resolver that contains authorization function based on {@link OrgAuthorization} annotated value
 *
 * @author DreamJM
 */
public class OrgAuthCheckRequestParamResolver extends RequestParamMethodArgumentResolver {

    private RequestParamMethodArgumentResolver resolver;

    private AuthorizationService service;

    public OrgAuthCheckRequestParamResolver(ConfigurableBeanFactory beanFactory, RequestParamMethodArgumentResolver resolver,
                                            AuthorizationService service) {
        super(beanFactory, false);
        this.resolver = resolver;
        this.service = service;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return resolver.supportsParameter(parameter);
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
                if (result == null) {
                    service.authorize(authUser, null, authAnnotation, roleAnnotation);
                } else if (result.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(result); i++) {
                        service.authorize(authUser, String.valueOf(Array.get(result, i)), authAnnotation, roleAnnotation);
                    }
                } else {
                    service.authorize(authUser, String.valueOf(result), authAnnotation, roleAnnotation);
                }
            }
        }
        return result;
    }
}
