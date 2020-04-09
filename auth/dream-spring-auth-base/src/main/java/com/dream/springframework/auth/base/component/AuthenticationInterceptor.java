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
import com.dream.springframework.auth.base.annotation.AuthIgnore;
import com.dream.springframework.auth.base.annotation.OrgAuthorization;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.auth.base.service.AuthenticationService;
import com.dream.springframework.auth.base.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User authentication handler interceptor
 *
 * @author DreamJM
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private AuthenticationService<?> authenticationService;

    private AuthorizationService authorizationService;

    private Map<Method, Boolean> orgCheckMethodMap = new ConcurrentHashMap<>();

    /**
     * @param authenticationService user authentication service
     * @param authorizationService  authorization for authorities and roles
     */
    public AuthenticationInterceptor(AuthenticationService<?> authenticationService, AuthorizationService authorizationService) {
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthIgnore ignoreAnnotation;
        RequiredAuthorities authAnnotation;
        RequiredRoles roleAnnotation;
        HandlerMethod method;
        boolean orgCheck = false;
        if (handler instanceof HandlerMethod) {
            method = ((HandlerMethod) handler);
            ignoreAnnotation = method.getMethodAnnotation(AuthIgnore.class);
            authAnnotation = method.getMethodAnnotation(RequiredAuthorities.class);
            roleAnnotation = method.getMethodAnnotation(RequiredRoles.class);
            // Caches and judge OrgAuthCheck Tag for methods
            if (authAnnotation != null || roleAnnotation != null) {
                Boolean orgCheckBool = orgCheckMethodMap.get(method.getMethod());
                if (orgCheckBool != null) {
                    orgCheck = orgCheckBool;
                } else if (method.hasMethodAnnotation(OrgAuthorization.class)) {
                    orgCheck = true;
                } else {
                    for (MethodParameter parameter : method.getMethodParameters()) {
                        if (parameter.hasParameterAnnotation(OrgAuthorization.class)) {
                            orgCheck = true;
                        }
                    }
                    orgCheckMethodMap.put(method.getMethod(), orgCheck);
                }
            }
        } else {
            return true;
        }

        //If AuthIgnore annotation contained，skip authorization
        if (ignoreAnnotation != null) {
            return true;
        }

        BaseAuthUser authUser = authenticationService.authenticate(request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("Request {} was authorized for user: {}", request.getRequestURI(), authUser.getUid());
        }
        // If not need to check basing on organization, checking here. Or else, delay to corresponding Resolver or Advice
        if (!orgCheck) {
            authorizationService.authorize(authUser, null, authAnnotation, roleAnnotation);
        }
        request.setAttribute(BaseAuthUser.USER_KEY, authUser);
        return true;
    }
}
