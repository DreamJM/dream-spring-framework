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

package com.dream.springframework.auth.base.annotation;

import java.util.function.BinaryOperator;

/**
 * Authorities or Roles combination logic
 *
 * @author DreamJM
 */
public enum Logical {

    /**
     * 'and' logical operation
     */
    AND,
    /**
     * 'or' logical operation
     */
    OR;

    /**
     * Provides boolean binary operator for the {@link #AND} and {@link #OR} logic
     *
     * @return boolean binary operator
     */
    public BinaryOperator<Boolean> judge() {
        if (this.equals(Logical.AND)) {
            return (b1, b2) -> b1 && b2;
        } else {
            return (b1, b2) -> b1 || b2;
        }
    }
}
