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

package com.dream.springframework.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Page information for data list result
 *
 * @param <T> data type inside page
 * @author DreamJM
 */
@ApiModel("Page Info")
public class PageResult<T> {

    @ApiModelProperty("current page number")
    private Integer pageNum;

    @ApiModelProperty("data count per page")
    private Integer pageSize;

    @ApiModelProperty("total page count")
    private Integer pages;

    @ApiModelProperty("total data count")
    private Long total;

    @ApiModelProperty("data list")
    private List<T> values;

    public PageResult(List<T> values) {
        this.values = values;
        this.total = values == null ? 0L : values.size();
    }

    public PageResult(Integer pageNum, Integer pageSize, Integer pages, Long total, List<T> values) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.total = total;
        this.values = values;
    }

    /**
     * @return current page number
     */
    public Integer getPageNum() {
        return pageNum;
    }

    /**
     * @return data count per page
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @return total page count
     */
    public Integer getPages() {
        return pages;
    }

    /**
     * @return total data count
     */
    public Long getTotal() {
        return total;
    }

    /**
     * @return data list result
     */
    public List<T> getValues() {
        return values;
    }
}
