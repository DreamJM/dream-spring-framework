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

import com.dream.springframework.auth.base.BaseAuthUser;
import com.dream.springframework.auth.base.annotation.Logical;
import com.dream.springframework.auth.base.annotation.LoginUser;
import com.dream.springframework.auth.base.annotation.RequiredAuthorities;
import com.dream.springframework.auth.base.annotation.RequiredRoles;
import com.dream.springframework.auth.token.model.TokenAuthUser;
import com.dream.springframework.base.model.Success;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DreamJM
 */
@Api(tags = "Demo User API")
@RestController
public class UserController {

    @GetMapping("/api/session")
    public BaseAuthUser getLoginUser(@LoginUser TokenAuthUser authUser) {
        return authUser;
    }

    @RequiredAuthorities({"auth1", "auth3"})
    @GetMapping("/api/authority-check")
    public Success checkAuthorities() {
        return new Success();
    }

    @RequiredAuthorities(value = {"auth1", "auth3"}, logical = Logical.AND)
    @GetMapping("/api/authority-check-and")
    public Success checkAuthoritiesAnd() {
        return new Success();
    }

    @RequiredRoles({"role1", "role3"})
    @GetMapping("/api/role-check")
    public Success checkRole() {
        return new Success();
    }


    @RequiredRoles(value = {"role1", "role3"}, logical = Logical.AND)
    @GetMapping("/api/role-check-and")
    public Success checkRoleAnd() {
        return new Success();
    }

}
