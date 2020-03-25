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
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

/**
 * Path variable resolver that contains authorization function based on {@link OrgAuthorization} annotated value
 *
 * @author DreamJM
 */
public class OrgCheckPathVariableResolver extends PathVariableMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(OrgCheckPathVariableResolver.class);

    private AuthorizationService authService;

    private OrgPermissionService<?> permissionService;

    public OrgCheckPathVariableResolver(AuthorizationService authService, OrgPermissionService<?> permissionService) {
        this.authService = authService;
        this.permissionService = permissionService;
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Object result = super.resolveName(name, parameter, request);
        if (parameter.getMethod() == null) {
            return result;
        }
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
            authService.authorize(authUser, result == null ? null : String.valueOf(result), authAnnotation, roleAnnotation);
        }
        if (annPrm != null) {
            if (permissionService == null) {
                logger.warn("@OrgPermission Annotation used, but no OrgPermissionService found");
            } else {
                String orgId = result == null ? null : String.valueOf(result);
                if (Strings.isNullOrEmpty(orgId) && !annPrm.skipEmpty()) {
                    throw new ForbiddenException(BaseErrorCode.EMPTY_ORG_FORBIDDEN);
                }
                permissionService.checkOrgResourcePermission(authUser.getOrgIds(), orgId);
            }
        }
        return result;
    }
}
