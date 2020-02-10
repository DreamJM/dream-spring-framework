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

import com.dream.springframework.auth.base.model.CheckedOrgList;

import java.util.Collection;
import java.util.List;

/**
 * @author DreamJM
 */
public class OrgDemoList implements CheckedOrgList<OrgDemo> {

    private List<OrgDemo> orgs;

    @Override
    public Collection<OrgDemo> getCheckOrgs() {
        return orgs;
    }

    public List<OrgDemo> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<OrgDemo> orgs) {
        this.orgs = orgs;
    }
}
