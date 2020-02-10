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

package com.dream.springframework.auth.base.service;

import com.dream.springframework.auth.base.model.CheckedOrg;
import com.dream.springframework.base.exception.RequestException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Service for organization resource permission checking
 * <p>
 * To enable organization resource permission checking, should implement OrgCheckService interface and inject it into spring context.
 *
 * @param <T> check passed organization model type (should conform to the generic type defined in {@link CheckedOrg})
 * @author DreamJM
 */
public interface OrgPermissionService<T> {

    /**
     * Organization with identity to check
     *
     * @param orgIds     authorized organization identities
     * @param checkOrgId organization id to be checked
     * @return check passed organization
     * @throws RequestException resource permission checking exception
     */
    T checkOrgResourcePermission(Set<String> orgIds, String checkOrgId) throws RequestException;

    /**
     * Organization list with identities to check
     *
     * @param orgIds      authorized organization identities
     * @param checkOrgIds organization identities to be checked
     * @return check passed organization
     * @throws RequestException resource permission checking exception
     */
    default List<T> checkOrgsResourcePermission(Set<String> orgIds, Collection<String> checkOrgIds) throws RequestException {
        List<T> checkedOrgs = new ArrayList<>();
        if (checkOrgIds != null) {
            for (String checkOrgId : checkOrgIds) {
                checkedOrgs.add(checkOrgResourcePermission(orgIds, checkOrgId));
            }
        }
        return checkedOrgs;
    }

}
