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

import com.dream.springframework.demo.domain.entity.DemoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author DreamJM
 */
@ApiModel("Demo Item")
public class Demo {

    @ApiModelProperty("Demo ID")
    private Long id;

    @NotBlank(message = "bind.name.required")
    @ApiModelProperty("Demo Item Name")
    private String name;

    @ApiModelProperty("Greeting words")
    private String greeting;

    public Demo() {
    }

    public Demo(Long id, String name, String greeting) {
        this.id = id;
        this.name = name;
        this.greeting = greeting;
    }

    public Demo(DemoEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.greeting = entity.getGreeting();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
