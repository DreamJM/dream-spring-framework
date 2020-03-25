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
import com.dream.springframework.auth.base.annotation.OrgPermission;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.auth.base.service.AuthorizationService;
import com.dream.springframework.auth.base.service.OrgPermissionService;
import com.dream.springframework.base.exception.BaseErrorCode;
import com.dream.springframework.base.exception.ForbiddenException;
import com.dream.springframework.base.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Request param resolver that contains authorization function based on {@link OrgAuthorization} annotated value
 *
 * @author DreamJM
 */
public class OrgCheckRequestParamResolver extends RequestParamMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(OrgCheckRequestParamResolver.class);

    private RequestParamMethodArgumentResolver resolver;

    private AuthorizationService authService;

    private OrgPermissionService<?> permissionService;

    public OrgCheckRequestParamResolver(ConfigurableBeanFactory beanFactory, RequestParamMethodArgumentResolver resolver,
                                        AuthorizationService authService, OrgPermissionService<?> permissionService) {
        super(beanFactory, false);
        this.resolver = resolver;
        this.authService = authService;
        this.permissionService = permissionService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return resolver.supportsParameter(parameter);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Object result = super.resolveName(name, parameter, request);
        OrgAuthorization orgAuthCheck = parameter.getParameterAnnotation(OrgAuthorization.class);
        OrgPermission annPrm = parameter.getParameterAnnotation(OrgPermission.class);
        if (orgAuthCheck == null && annPrm == null) {
            return result;
        }
        BaseAuthUser authUser = (BaseAuthUser) request.getAttribute(BaseAuthUser.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (authUser == null) {
            throw new UnauthorizedException(BaseErrorCode.AUTH_FAILURE);
        }
        if (orgAuthCheck != null) {
            RequiredAuthorities authAnnotation = parameter.getMethodAnnotation(RequiredAuthorities.class);
            RequiredRoles roleAnnotation = parameter.getMethodAnnotation(RequiredRoles.class);
            if (result == null) {
                authService.authorize(authUser, null, authAnnotation, roleAnnotation);
            } else if (result.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(result); i++) {
                    authService.authorize(authUser, String.valueOf(Array.get(result, i)), authAnnotation, roleAnnotation);
                }
            } else {
                authService.authorize(authUser, String.valueOf(result), authAnnotation, roleAnnotation);
            }
        }
        if (annPrm != null) {
            if (permissionService == null) {
                logger.warn("@OrgPermission Annotation used, but no OrgPermissionService been found");
                return result;
            }
            if (result == null) {
                if (!annPrm.skipEmpty()) {
                    throw new ForbiddenException(BaseErrorCode.EMPTY_ORG_FORBIDDEN);
                }
                permissionService.checkOrgResourcePermission(authUser.getOrgIds(), null);
            } else if (result.getClass().isArray()) {
                List<String> checkOrgIds = new ArrayList<>();
                for (int i = 0; i < Array.getLength(result); i++) {
                    checkOrgIds.add(String.valueOf(Array.get(result, i)));
                }
                permissionService.checkOrgsResourcePermission(authUser.getOrgIds(), checkOrgIds);
            } else {
                String orgId = String.valueOf(result);
                if ("".equals(orgId) && !annPrm.skipEmpty()) {
                    throw new ForbiddenException(BaseErrorCode.EMPTY_ORG_FORBIDDEN);
                }
                permissionService.checkOrgResourcePermission(authUser.getOrgIds(), String.valueOf(result));
            }
        }
        return result;
    }
}
