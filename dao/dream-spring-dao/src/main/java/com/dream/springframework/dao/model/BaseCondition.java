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

package com.dream.springframework.dao.model;

/**
 * Base Query Condition for sql
 *
 * @author DreamJM
 */
public class BaseCondition {

    /**
     * Page number for pagination
     */
    private Integer pageNum;

    /**
     * Page size for pagination
     */
    private Integer pageSize;

    /**
     * Order String for sql
     */
    private String order;

    /**
     * @return page number for pagination
     */
    public Integer getPageNum() {
        return pageNum;
    }

    /**
     * @param pageNum page number for pagination to set
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * @return page size for pagination
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize page size for pagination to set
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return order string for sql
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order order string for sql to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Skips the query action if satisfies some condition.
     *
     * @return true to skip query action
     */
    public boolean skipQuery() {
        return false;
    }
}
