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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Action hint item information
 *
 * @author DreamJM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface ActionLogHint {

    /**
     * @return hint title name.
     */
    String name();

    /**
     * @return input parameter number
     */
    int index() default 0;

    /**
     * Whether to use the return value. If true, {@link #index()} will be ignored
     *
     * @return whether to use the return value
     */
    boolean isReturn() default false;

    /**
     * Field in the object which specified by {@link #isReturn()} or {@link #index()} will be used as the value of the {@link #name()}.
     * <ul>
     * <li>Field should have getXxx or isXxx(boolean only) method</li>
     * <li>If the field you want to use is the sub field of an object field, you can use '.' to join them.
     * Example: field = "field1.field2"
     * </li>
     * </ul>
     *
     * @return field name
     */
    String value() default "";

}
