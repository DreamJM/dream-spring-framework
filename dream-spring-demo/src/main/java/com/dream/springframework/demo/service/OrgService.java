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

package com.dream.springframework.demo.service;

import com.dream.springframework.actionlog.annotation.ActionLog;
import com.dream.springframework.actionlog.annotation.ActionLogHint;
import com.dream.springframework.actionlog.annotation.ActionValue;
import com.dream.springframework.auth.base.annotation.OrgAuthorization;
import com.dream.springframework.auth.base.service.OrgPermissionService;
import com.dream.springframework.base.exception.ForbiddenException;
import com.dream.springframework.base.exception.RequestException;
import com.dream.springframework.demo.constant.ErrorCode;
import com.dream.springframework.demo.model.OrgDemo;
import com.dream.springframework.demo.model.OrgDemoList;
import com.dream.springframework.demo.model.Organ;
import com.dream.springframework.demo.model.OrgsDemo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author DreamJM
 */
@Service
public class OrgService implements OrgPermissionService<Organ> {

    @ActionLog(moduleId = "org", funcType = "update", hints = @ActionLogHint(name = "demo", value = "orgId"), values = @ActionValue(isReturn = true))
    public Organ update(OrgDemo orgDemo) {
        return new Organ(orgDemo.getCheckOrgId());
    }

    @ActionLog(moduleId = "org", funcType = "update", hints = @ActionLogHint(name = "demo", isReturn = true, value = "id"), values = @ActionValue(isReturn = true))
    public List<Organ> updateOrgs(@OrgAuthorization @RequestBody OrgsDemo orgsDemo) {
        return orgsDemo.getCheckedOrgs();
    }

    @ActionLog(moduleId = "org", funcType = "update", hints = @ActionLogHint(name = "demo", isReturn = true, value = "id"), values = @ActionValue(isReturn = true))
    public Organ[] updateOrgs(OrgDemoList orgDemoList) {
        return orgDemoList.getCheckOrgs().stream().map(OrgDemo::getCheckedOrg).toArray(Organ[]::new);
    }

    @Override
    public Organ checkOrgResourcePermission(Set<String> orgIds, String checkOrgId) throws RequestException {
        if (!orgIds.contains(checkOrgId)) {
            throw new ForbiddenException(ErrorCode.ORG_ACCESS_FORBIDDEN);
        }
        return new Organ(checkOrgId);
    }
}
