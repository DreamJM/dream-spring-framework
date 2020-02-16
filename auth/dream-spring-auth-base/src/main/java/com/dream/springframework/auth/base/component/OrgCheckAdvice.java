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
import com.dream.springframework.auth.base.annotation.*;
import com.dream.springframework.auth.base.model.CheckedOrg;
import com.dream.springframework.auth.base.model.CheckedOrgList;
import com.dream.springframework.auth.base.model.CheckedOrgs;
import com.dream.springframework.auth.base.service.AuthorizationService;
import com.dream.springframework.auth.base.service.OrgPermissionService;
import com.dream.springframework.base.exception.RequestException;
import com.dream.springframework.base.exception.RuntimeRequestException;
import com.google.common.base.Strings;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Checks organization resource permission
 *
 * @author DreamJM
 */
@ControllerAdvice
public class OrgCheckAdvice extends RequestBodyAdviceAdapter {

    private HttpServletRequest request;

    private OrgPermissionService<?> permissionService;

    private AuthorizationService authService;

    public OrgCheckAdvice(ObjectProvider<OrgPermissionService<?>> permissionProvider, AuthorizationService authService,
                          HttpServletRequest request) {
        this.permissionService = permissionProvider.getIfAvailable();
        this.authService = authService;
        this.request = request;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (methodParameter.getMethod() == null || methodParameter.getMethodAnnotation(AuthIgnore.class) != null) {
            return false;
        }
        if (permissionService == null && !methodParameter.hasParameterAnnotation(OrgAuthorization.class)) {
            return false;
        }
        if (targetType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) targetType;
            if (parameterizedType.getRawType() instanceof Class && Collection.class
                    .isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                Type[] genericTypes = parameterizedType.getActualTypeArguments();
                if (genericTypes != null && genericTypes.length > 0 && genericTypes[0] instanceof Class) {
                    return checkClassType((Class<?>) genericTypes[0]);
                }
            }
            return false;
        }
        Class<?> targetClass = (Class<?>) targetType;
        if (targetClass.isArray()) {
            targetClass = targetClass.getComponentType();
        }
        return checkClassType(targetClass);
    }

    private boolean checkClassType(Class<?> targetClass) {
        return CheckedOrg.class.isAssignableFrom(targetClass) || CheckedOrgList.class.isAssignableFrom(targetClass) || CheckedOrgs.class
                .isAssignableFrom(targetClass);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        BaseAuthUser authUser = (BaseAuthUser) request.getAttribute(BaseAuthUser.USER_KEY);
        if (parameter.getMethod() == null || authUser == null) {
            return body;
        }
        if (parameter.hasMethodAnnotation(OrgPermissionIgnore.class) && !parameter.hasParameterAnnotation(OrgAuthorization.class)) {
            return body;
        }
        RequiredAuthorities authAnnotation = parameter.getMethodAnnotation(RequiredAuthorities.class);
        RequiredRoles roleAnnotation = parameter.getMethodAnnotation(RequiredRoles.class);
        try {
            checkAuthAndPermission(body, parameter, authUser, authAnnotation, roleAnnotation);
        } catch (RequestException ex) {
            throw new RuntimeRequestException(ex);
        }
        return body;
    }

    private void checkAuthAndPermission(Object body, MethodParameter parameter, BaseAuthUser authUser, RequiredAuthorities authAnnotation,
                                        RequiredRoles roleAnnotation) throws RequestException {
        if (body instanceof CheckedOrg) {
            checkOrg((CheckedOrg) body, parameter, authUser, authAnnotation, roleAnnotation);
        } else if (body instanceof CheckedOrgs) {
            checkOrgs((CheckedOrgs) body, parameter, authUser, authAnnotation, roleAnnotation);
        } else if (body instanceof CheckedOrgList) {
            checkOrgList((CheckedOrgList) body, parameter, authUser, authAnnotation, roleAnnotation);
        } else if (body.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(body); i++) {
                checkAuthAndPermission(Array.get(body, i), parameter, authUser, authAnnotation, roleAnnotation);
            }
        } else if (body instanceof Collection) {
            Collection<?> collection = (Collection<?>) body;
            for (Object item : collection) {
                checkAuthAndPermission(item, parameter, authUser, authAnnotation, roleAnnotation);
            }
        }
    }

    private void checkOrg(CheckedOrg orgCheck, MethodParameter parameter, BaseAuthUser authUser, RequiredAuthorities authAnnotation,
                          RequiredRoles roleAnnotation) throws RequestException {
        if (permissionService != null && !parameter.hasMethodAnnotation(OrgPermissionIgnore.class) && !Strings
                .isNullOrEmpty(orgCheck.getCheckOrgId())) {
            //noinspection unchecked
            orgCheck.setCheckedOrg(permissionService.checkOrgResourcePermission(authUser.getOrgIds(), orgCheck.getCheckOrgId()));
        }
        if (parameter.hasParameterAnnotation(OrgAuthorization.class)) {
            authService.authorize(authUser, orgCheck.getCheckOrgId(), authAnnotation, roleAnnotation);
        }
    }

    private void checkOrgs(CheckedOrgs orgsCheck, MethodParameter parameter, BaseAuthUser authUser, RequiredAuthorities authAnnotation,
                           RequiredRoles roleAnnotation) throws RequestException {
        if (permissionService != null && !parameter.hasMethodAnnotation(OrgPermissionIgnore.class) && orgsCheck
                .getCheckOrgIds() != null && !orgsCheck.getCheckOrgIds().isEmpty()) {
            //noinspection unchecked
            orgsCheck.setCheckedOrgs(permissionService.checkOrgsResourcePermission(authUser.getOrgIds(), orgsCheck.getCheckOrgIds()));
        }
        if (parameter.hasParameterAnnotation(OrgAuthorization.class)) {
            if (authAnnotation == null && roleAnnotation == null) {
                return;
            }
            if (orgsCheck.getCheckOrgIds() == null || orgsCheck.getCheckOrgIds().isEmpty()) {
                authService.authorize(authUser, null, authAnnotation, roleAnnotation);
            } else {
                for (Object checkOrgId : orgsCheck.getCheckOrgIds()) {
                    authService.authorize(authUser, (String) checkOrgId, authAnnotation, roleAnnotation);
                }
            }
        }
    }

    private void checkOrgList(CheckedOrgList orgCheckList, MethodParameter parameter, BaseAuthUser authUser,
                              RequiredAuthorities authAnnotation,
                              RequiredRoles roleAnnotation) throws RequestException {
        if (permissionService != null && !parameter.hasMethodAnnotation(OrgPermissionIgnore.class) && orgCheckList
                .getCheckOrgs() != null && !orgCheckList.getCheckOrgs().isEmpty()) {
            for (Object obj : orgCheckList.getCheckOrgs()) {
                CheckedOrg orgCheck = (CheckedOrg) obj;
                //noinspection unchecked
                orgCheck.setCheckedOrg(permissionService.checkOrgResourcePermission(authUser.getOrgIds(), orgCheck.getCheckOrgId()));
            }
        }
        if (parameter.hasParameterAnnotation(OrgAuthorization.class)) {
            if (authAnnotation == null && roleAnnotation == null) {
                return;
            }
            if (orgCheckList.getCheckOrgs() == null || orgCheckList.getCheckOrgs().isEmpty()) {
                authService.authorize(authUser, null, authAnnotation, roleAnnotation);
            } else {
                for (Object obj : orgCheckList.getCheckOrgs()) {
                    CheckedOrg orgCheck = (CheckedOrg) obj;
                    authService.authorize(authUser, orgCheck.getCheckOrgId(), authAnnotation, roleAnnotation);
                }
            }
        }
    }
}
