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

import java.util.*;

/**
 * Base query type builder
 *
 * @param <T> Query Class Type
 * @author DreamJM
 */
public class BaseQueryBuilder<T extends BaseCondition> {

    private static final String SQL_ORDER_DESC = " DESC";

    private static final String SQL_ORDER_ASC = " ASC";

    /**
     * Query Object
     */
    protected T query;

    public BaseQueryBuilder(T query) {
        this.query = query;
    }

    /**
     * Page information
     *
     * @param pageNum  page number to set
     * @param pageSize page size to set
     */
    protected void setPage(Integer pageNum, Integer pageSize) {
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
    }

    /**
     * Convert single order information to order sql string and set the result to query object
     *
     * @param order single order information
     */
    protected void buildOrder(Order order) {
        if (order != null) {
            buildOrders(Collections.singletonList(order));
        } else {
            Order defaultOrder = getDefaultOrder();
            if (defaultOrder != null) {
                buildOrders(Collections.singletonList(defaultOrder));
            }
        }
    }

    /**
     * Convert orders information to order sql string and set the result to query object
     *
     * @param targetOrders orders information
     */
    protected void buildOrders(List<Order> targetOrders) {
        Map<String, OrderColumn[]> orderColumnMap = getOrderColumnMap();
        if (orderColumnMap == null) {
            return;
        }
        if (targetOrders == null || targetOrders.isEmpty()) {
            Order defaultOrder = getDefaultOrder();
            if (defaultOrder != null) {
                targetOrders = Collections.singletonList(defaultOrder);
            } else {
                return;
            }
        }
        query.setOrder(targetOrders.stream().filter(targetOrder -> orderColumnMap.containsKey(targetOrder.getOrderBy()))
                .flatMap(targetOrder -> Arrays.stream(orderColumnMap.get(targetOrder.getOrderBy())).filter(Objects::nonNull)
                        .map(column -> new OrderColumn(column, targetOrder.getOrder()))).sorted(
                        (o1, o2) -> o1.isAppend() ^ o2.isAppend() ? (o1.isAppend() ? 1 : -1) : 0).distinct()
                .map(orderColumn -> orderColumn.getOrderBy() + (orderColumn
                        .getOrder() == Order.ORDER_DESC ? SQL_ORDER_DESC : SQL_ORDER_ASC)).reduce((s1, s2) -> s1 + "," + s2).orElse(null));
    }

    /**
     * @return order column map used to convert order to order column sql
     */
    protected Map<String, OrderColumn[]> getOrderColumnMap() {
        return null;
    }

    /**
     * @return default order when no order applied
     */
    protected Order getDefaultOrder() {
        return null;
    }

    /**
     * Builds query condition object
     *
     * @return query condition object
     */
    public T build() {
        return query;
    }
}
