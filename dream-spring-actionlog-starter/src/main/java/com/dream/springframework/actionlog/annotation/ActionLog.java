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

package com.dream.springframework.actionlog.annotation;

import java.lang.annotation.*;

/**
 * Action log annotation
 *
 * @author DreamJM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionLog {

    /**
     * @return action related module id
     */
    String moduleId();

    /**
     * @return action type
     */
    String funcType();

    /**
     * Action content abbreviated information.
     * <p>
     * Each action log hint represents an attribute that action concerned. Hints will be joined by ','.<br/>
     * For example: name:xxx, age:18
     *
     * @return action content abbreviated information
     */
    ActionLogHint[] hints() default {};

    /**
     * Action detail value.
     * <p>
     * The detail value objects will be serialized as json
     *
     * @return action detail value
     */
    ActionValue[] values() default {};
}
