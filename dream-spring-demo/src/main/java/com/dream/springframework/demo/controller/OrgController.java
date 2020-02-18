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

package com.dream.springframework.demo.controller;

import com.dream.springframework.auth.base.annotation.OrgAuthorization;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.demo.model.OrgDemo;
import com.dream.springframework.demo.model.OrgDemoList;
import com.dream.springframework.demo.model.Organ;
import com.dream.springframework.demo.model.OrgsDemo;
import com.dream.springframework.demo.service.OrgService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DreamJM
 */
@Api(tags = "Demo Organization API")
@RestController
public class OrgController {

    private OrgService service;

    public OrgController(OrgService service) {
        this.service = service;
    }

    @RequiredAuthorities("auth1")
    @PostMapping("/api/org")
    public Organ updateOrg(@OrgAuthorization @RequestBody OrgDemo orgDemo) {
        return service.update(orgDemo);
    }

    @RequiredRoles("role1")
    @PostMapping("/api/orgs")
    public List<Organ> updateOrgs(@OrgAuthorization @RequestBody OrgsDemo orgsDemo) {
        return service.updateOrgs(orgsDemo);
    }

    @RequiredAuthorities("auth1")
    @PostMapping("/api/org-list")
    public Organ[] updateOrgs(@RequestBody OrgDemoList orgDemoList) {
        return service.updateOrgs(orgDemoList);
    }

    @RequiredAuthorities("auth3")
    @GetMapping("/api/orgs/{id}")
    public Organ getOrg(@OrgAuthorization @PathVariable String id) {
        return new Organ(id);
    }

    @RequiredRoles("role3")
    @GetMapping("/api/org")
    public Organ getOrgById(@OrgAuthorization @RequestParam(required = false) String id) {
        return new Organ(id);
    }

    @GetMapping("/api/org-rsp")
    public OrgDemo testRespOrg(@RequestParam String orgId) {
        OrgDemo orgDemo = new OrgDemo();
        orgDemo.setOrgId(orgId);
        return orgDemo;
    }

    @RequiredAuthorities("auth3")
    @GetMapping("/api/orgs-rsp")
    public @OrgAuthorization List<OrgDemo> testRespOrg(@RequestParam String[] orgIds) {
        List<OrgDemo> orgs = new ArrayList<>();
        for (String orgId : orgIds) {
            OrgDemo orgDemo = new OrgDemo();
            orgDemo.setOrgId(orgId);
            orgs.add(orgDemo);
        }
        return orgs;
    }
}
