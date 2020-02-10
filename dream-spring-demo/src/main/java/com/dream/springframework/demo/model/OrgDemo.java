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

package com.dream.springframework.demo.model;

import com.dream.springframework.auth.base.model.CheckedOrg;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author DreamJM
 */
public class OrgDemo implements CheckedOrg<Organ> {

    private String orgId;

    @JsonIgnore
    private Organ checkedOrg;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Organ getCheckedOrg() {
        return checkedOrg;
    }

    @Override
    public String getCheckOrgId() {
        return orgId;
    }

    @Override
    public void setCheckedOrg(Organ checkedOrg) {
        this.checkedOrg = checkedOrg;
    }
}
