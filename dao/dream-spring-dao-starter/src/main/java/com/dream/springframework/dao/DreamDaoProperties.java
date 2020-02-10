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

package com.dream.springframework.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Dream sql dao properties
 *
 * @author DreamJM
 */
@ConfigurationProperties(prefix = "dream.dao")
public class DreamDaoProperties {

    /**
     * Default page size when request page size is null
     */
    private int defaultPageSize = 10;

    /**
     * @return default page size when request page size is null
     */
    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    /**
     * @param defaultPageSize default page size when request page size is null tor set
     */
    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }
}
