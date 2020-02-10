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

package com.dream.springframework.dao.util;

import com.dream.springframework.base.model.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Page utilities used {@link PageHelper}
 *
 * @author DreamJM
 * @see PageHelper
 */
public class PageUtil {

    private static int DEFAULT_PAGE_SIZE = 10;

    /**
     * @param defaultPageSize default page size
     */
    public static void setDefaultPageSize(int defaultPageSize) {
        PageUtil.DEFAULT_PAGE_SIZE = defaultPageSize;
    }

    /**
     * Starts paging with {@link PageHelper}
     * <p>
     * Uses default page size when page size is null.<br/>
     * Skip paging when page number is null;
     *
     * @param pageNum  page number
     * @param pageSize page size
     * @return whether page condition applied({@code false} if pageNum is {@code null})
     */
    public static boolean startPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            return false;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PageHelper.startPage(pageNum, pageSize);
        return true;
    }

    /**
     * Converts {@link Page} to {@link PageResult}
     *
     * @param page database query page result
     * @param <T>  database entity
     * @return Page Result
     */
    public static <T> PageResult<T> parsePageResult(Page<T> page) {
        if (page.getPageNum() > 0) {
            return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), page.getResult());
        } else {
            return new PageResult<>(page.getResult());
        }
    }

    /**
     * Converts {@link Page} to {@link PageResult}
     *
     * @param page      database query page result
     * @param converter converter to convert query database entity to view model
     * @param <T>       database entity
     * @param <R>       view model
     * @return Page Result
     */
    public static <T, R> PageResult<R> parsePageResult(Page<T> page, Function<T, @Nullable R> converter) {
        if (page.getPageNum() > 0) {
            return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(),
                    page.getResult().stream().map(converter).collect(Collectors.toList()));
        } else {
            return new PageResult<>(page.getResult().stream().map(converter).collect(Collectors.toList()));
        }
    }

}
