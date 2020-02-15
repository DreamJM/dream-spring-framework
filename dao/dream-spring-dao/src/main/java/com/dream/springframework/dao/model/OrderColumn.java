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

import java.util.Objects;

/**
 * Definition of the column ordered by
 *
 * @author DreamJM
 */
public class OrderColumn {

    /**
     * sql order field
     */
    private String orderBy;

    /**
     * Order
     * <p>
     * If null, the order specified by the original request {@link Order#getOrder() order} will be used for ordering
     * <p>
     * Ascending Order: {@link Order#ORDER_ASC 0}<br>
     * Descending Order: {@link Order#ORDER_DESC 1}
     *
     * @see Order#ORDER_ASC
     * @see Order#ORDER_DESC
     */
    private Integer order;

    /**
     * Whether is appended field
     * <p>
     * If true, this order column will be put behind.
     */
    private boolean append;

    /**
     * @param orderBy sql order field
     */
    public OrderColumn(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @param orderBy sql order field
     * @param order   {@link Order#ORDER_ASC 0}: Ascending Order,
     *                {@link Order#ORDER_DESC 1}: Descending Order
     *                {@code null}: the order specified by the original request {@link Order#getOrder() order} will be used for ordering
     */
    public OrderColumn(String orderBy, Integer order) {
        this.orderBy = orderBy;
        this.order = order;
    }

    /**
     * @param orderBy sql order field
     * @param append  If true, this order column will be put behind
     */
    public OrderColumn(String orderBy, boolean append) {
        this.orderBy = orderBy;
        this.append = append;
    }

    /**
     * @param orderBy sql order field
     * @param order   {@link Order#ORDER_ASC 0}: Ascending Order,
     *                {@link Order#ORDER_DESC 1}: Descending Order
     *                {@code null}: the order specified by the original request {@link Order#getOrder() order} will be used for ordering
     * @param append  If true, this order column will be put behind
     */
    public OrderColumn(String orderBy, Integer order, boolean append) {
        this.orderBy = orderBy;
        this.order = order;
        this.append = append;
    }

    OrderColumn(OrderColumn column, int order) {
        this.orderBy = column.getOrderBy();
        this.append = column.isAppend();
        this.order = column.getOrder();
        if (this.order == null) {
            this.order = order;
        }
    }

    /**
     * @return sql order field
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * If null, the order specified by the original request {@link Order#getOrder() order} will be used for ordering
     * <p>
     * Ascending Order: {@link Order#ORDER_ASC 0}<br>
     * Descending Order: {@link Order#ORDER_DESC 1}
     *
     * @return order
     * @see Order#ORDER_ASC
     * @see Order#ORDER_DESC
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * If true, this order column will be put behind.
     *
     * @return Whether is appended field
     */
    public boolean isAppend() {
        return append;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderColumn that = (OrderColumn) o;
        return orderBy.equals(that.orderBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderBy);
    }
}
