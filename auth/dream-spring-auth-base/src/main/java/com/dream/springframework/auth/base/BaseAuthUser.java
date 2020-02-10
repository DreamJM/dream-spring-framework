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

package com.dream.springframework.auth.base;

import java.util.Map;
import java.util.Set;

/**
 * Base class type for authorized user.
 *
 * @author DreamJM
 */
public class BaseAuthUser {

    /**
     * Key used to store authorized user in HttpServletRequest as attribute
     */
    public static final String USER_KEY = "auth_user";

    /**
     * user identity
     */
    private String uid;

    /**
     * authorized organization identities
     */
    private Set<String> orgIds;

    /**
     * authorized roles for common resource
     */
    private Set<String> roleIds;

    /**
     * authorized authorities for common resource
     */
    private Set<String> authIds;

    /**
     * authorized roles based on organization
     */
    private Map<String, Set<String>> orgRoleMap;

    /**
     * authorized authorities based on organization
     */
    private Map<String, Set<String>> orgAuthMap;

    public BaseAuthUser(String uid) {
        this.uid = uid;
    }

    /**
     * @return authorized user identity
     */
    public String getUid() {
        return uid;
    }

    /**
     * @return authorized organization identities for user
     */
    public Set<String> getOrgIds() {
        return orgIds;
    }

    /**
     * @param orgIds authorized organization identities to set
     */
    public void setOrgIds(Set<String> orgIds) {
        this.orgIds = orgIds;
    }

    /**
     * @return authorized roles for user
     */
    public Set<String> getRoleIds() {
        return roleIds;
    }

    /**
     * @param roleIds authorized roles for user to set
     */
    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * @return authorized authorities for user
     */
    public Set<String> getAuthIds() {
        return authIds;
    }

    /**
     * @param authIds authorized authorities for user to set
     */
    public void setAuthIds(Set<String> authIds) {
        this.authIds = authIds;
    }

    /**
     * @return authorized roles based on organization
     */
    public Map<String, Set<String>> getOrgRoleMap() {
        return orgRoleMap;
    }

    /**
     * Each organization has their own roles
     *
     * @param orgRoleMap authorized roles based on organization to set
     */
    public void setOrgRoleMap(Map<String, Set<String>> orgRoleMap) {
        this.orgRoleMap = orgRoleMap;
    }

    /**
     * @return authorized authorities based on organization
     */
    public Map<String, Set<String>> getOrgAuthMap() {
        return orgAuthMap;
    }

    /**
     * Each organization has their own authorities
     *
     * @param orgAuthMap authorized roles based on organization to set
     */
    public void setOrgAuthMap(Map<String, Set<String>> orgAuthMap) {
        this.orgAuthMap = orgAuthMap;
    }
}
