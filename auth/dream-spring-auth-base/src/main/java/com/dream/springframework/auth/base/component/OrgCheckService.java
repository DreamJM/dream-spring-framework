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
import com.dream.springframework.base.exception.BaseErrorCode;
import com.dream.springframework.base.exception.ForbiddenException;
import com.dream.springframework.base.exception.RequestException;
import com.dream.springframework.base.exception.RuntimeRequestException;
import com.dream.springframework.base.util.StreamUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Base service for organization resource permission and organization based authorities and roles checking
 *
 * @author DreamJM
 */
class OrgCheckService {

    private static final Logger logger = LoggerFactory.getLogger(OrgCheckService.class);

    private HttpServletRequest request;

    private OrgPermissionService<?> permissionService;

    private AuthorizationService authService;

    private boolean returnValue;

    OrgCheckService(ObjectProvider<OrgPermissionService<?>> permissionProvider, AuthorizationService authService,
                    HttpServletRequest request, boolean returnValue) {
        this.permissionService = permissionProvider.getIfAvailable();
        this.authService = authService;
        this.request = request;
        this.returnValue = returnValue;
    }

    boolean supports(MethodParameter methodParameter) {
        if (methodParameter.getMethod() == null || methodParameter.getMethodAnnotation(AuthIgnore.class) != null) {
            return false;
        }
        if (orgPermissionAnnotated(methodParameter) == null && !orgAuthorizationAnnotated(methodParameter)) {
            return false;
        }
        if (methodParameter.getGenericParameterType() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) methodParameter.getGenericParameterType();
            if (parameterizedType.getRawType() instanceof Class && Collection.class
                    .isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                Type[] genericTypes = parameterizedType.getActualTypeArguments();
                if (genericTypes != null && genericTypes.length > 0 && genericTypes[0] instanceof Class) {
                    return checkClassType((Class<?>) genericTypes[0]);
                }
            }
            return false;
        }
        Class<?> targetClass = methodParameter.getParameterType();
        if (targetClass.isArray()) {
            targetClass = targetClass.getComponentType();
        }
        return checkClassType(targetClass);
    }

    private boolean checkClassType(Class<?> targetClass) {
        return CheckedOrg.class.isAssignableFrom(targetClass) || CheckedOrgList.class.isAssignableFrom(targetClass) || CheckedOrgs.class
                .isAssignableFrom(targetClass);
    }

    void checkAdvice(Object body, MethodParameter parameter) {
        BaseAuthUser authUser = (BaseAuthUser) request.getAttribute(BaseAuthUser.USER_KEY);
        if (parameter.getMethod() == null || authUser == null) {
            return;
        }
        OrgPermission orgPrmAnnotation = orgPermissionAnnotated(parameter);
        if (orgPrmAnnotation == null && !orgAuthorizationAnnotated(parameter)) {
            return;
        }
        RequiredAuthorities authAnnotation = parameter.getMethodAnnotation(RequiredAuthorities.class);
        RequiredRoles roleAnnotation = parameter.getMethodAnnotation(RequiredRoles.class);
        try {
            checkAuthAndPermission(body, parameter, authUser, authAnnotation, roleAnnotation, orgPrmAnnotation);
        } catch (RequestException ex) {
            throw new RuntimeRequestException(ex);
        }
    }

    private void checkAuthAndPermission(Object body, MethodParameter parameter, BaseAuthUser authUser, RequiredAuthorities authAnnotation,
                                        RequiredRoles roleAnnotation, OrgPermission orgPrmAnnotation) throws RequestException {
        if (body instanceof CheckedOrg) {
            checkOrg((CheckedOrg) body, parameter, authUser, authAnnotation, roleAnnotation, orgPrmAnnotation);
        } else if (body instanceof CheckedOrgs) {
            checkOrgs((CheckedOrgs) body, parameter, authUser, authAnnotation, roleAnnotation, orgPrmAnnotation);
        } else if (body instanceof CheckedOrgList) {
            checkOrgList((CheckedOrgList) body, parameter, authUser, authAnnotation, roleAnnotation, orgPrmAnnotation);
        } else if (body.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(body); i++) {
                checkAuthAndPermission(Array.get(body, i), parameter, authUser, authAnnotation, roleAnnotation, orgPrmAnnotation);
            }
        } else if (body instanceof Collection) {
            Collection<?> collection = (Collection<?>) body;
            for (Object item : collection) {
                checkAuthAndPermission(item, parameter, authUser, authAnnotation, roleAnnotation, orgPrmAnnotation);
            }
        }
    }

    private void checkOrg(CheckedOrg orgCheck, MethodParameter parameter, BaseAuthUser authUser, RequiredAuthorities authAnnotation,
                          RequiredRoles roleAnnotation, OrgPermission orgPrmAnnotation) throws RequestException {
        if (orgPrmAnnotation != null) {
            if (permissionService == null) {
                logger.warn("@OrgPermission Annotation used, but no OrgPermissionService found");
            } else {
                if (Strings.isNullOrEmpty(orgCheck.getCheckOrgId()) && !orgPrmAnnotation.skipEmpty()) {
                    throw new ForbiddenException(BaseErrorCode.EMPTY_ORG_FORBIDDEN);
                }
                //noinspection unchecked
                orgCheck.setCheckedOrg(permissionService.checkOrgResourcePermission(authUser.getOrgIds(), orgCheck.getCheckOrgId()));
            }
        }
        if (orgAuthorizationAnnotated(parameter)) {
            authService.authorize(authUser, orgCheck.getCheckOrgId(), authAnnotation, roleAnnotation);
        }
    }

    private void checkOrgs(CheckedOrgs orgsCheck, MethodParameter parameter, BaseAuthUser authUser, RequiredAuthorities authAnnotation,
                           RequiredRoles roleAnnotation, OrgPermission orgPrmAnnotation) throws RequestException {
        if (orgPrmAnnotation != null && orgsCheck.getCheckOrgIds() != null) {
            if (permissionService == null) {
                logger.warn("@OrgPermission Annotation used, but no OrgPermissionService found");
            } else {
                if (StreamUtils.isNullOrEmpty(orgsCheck.getCheckOrgIds()) && !orgPrmAnnotation.skipEmpty()) {
                    throw new ForbiddenException(BaseErrorCode.EMPTY_ORG_FORBIDDEN);
                }
                //noinspection unchecked
                orgsCheck.setCheckedOrgs(permissionService.checkOrgsResourcePermission(authUser.getOrgIds(), orgsCheck.getCheckOrgIds()));
            }
        }
        if (orgAuthorizationAnnotated(parameter)) {
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
                              RequiredAuthorities authAnnotation, RequiredRoles roleAnnotation, OrgPermission orgPrmAnnotation)
            throws RequestException {
        if (orgPrmAnnotation != null) {
            if (permissionService == null) {
                logger.warn("@OrgPermission Annotation used, but no OrgPermissionService found");
            } else {
                if (StreamUtils.isNullOrEmpty(orgCheckList.getCheckOrgs()) && !orgPrmAnnotation.skipEmpty()) {
                    throw new ForbiddenException(BaseErrorCode.EMPTY_ORG_FORBIDDEN);
                }
                for (Object obj : orgCheckList.getCheckOrgs()) {
                    CheckedOrg orgCheck = (CheckedOrg) obj;
                    //noinspection unchecked
                    orgCheck.setCheckedOrg(permissionService.checkOrgResourcePermission(authUser.getOrgIds(), orgCheck.getCheckOrgId()));
                }
            }
        }
        if (orgAuthorizationAnnotated(parameter)) {
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

    private boolean orgAuthorizationAnnotated(MethodParameter parameter) {
        return returnValue ? parameter.hasMethodAnnotation(OrgAuthorization.class) : parameter
                .hasParameterAnnotation(OrgAuthorization.class);
    }

    private OrgPermission orgPermissionAnnotated(MethodParameter parameter) {
        return returnValue ? parameter.getMethodAnnotation(OrgPermission.class) : parameter.getParameterAnnotation(OrgPermission.class);
    }

}
