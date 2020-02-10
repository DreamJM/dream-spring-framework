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

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Information
 *
 * @author DreamJM
 */
public class Order {

    private static final String DEFAULT_PARAM_SPLITTER = ":";

    private static final String DEFAULT_ITEM_SPLITTER = ",";

    /**
     * Ascending Order
     */
    public static final int ORDER_ASC = 0;

    /**
     * Descending Order
     */
    public static final int ORDER_DESC = 1;

    /**
     * Field name that ordered by
     */
    private String orderBy;

    /**
     * Order
     * <p>
     * Ascending Order: {@link #ORDER_ASC 0}<br/>
     * Descending Order: {@link #ORDER_DESC 1}
     *
     * @see #ORDER_ASC
     * @see #ORDER_DESC
     */
    private int order;

    public Order(String orderBy, int order) {
        this.orderBy = orderBy;
        this.order = order;
    }

    /**
     * @return Field name that ordered by
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Ascending Order: {@link #ORDER_ASC 0}<br/>
     * Descending Order: {@link #ORDER_DESC 1}
     *
     * @return order
     * @see #ORDER_ASC
     * @see #ORDER_DESC
     */
    public int getOrder() {
        return order;
    }

    /**
     * Parse order from string
     * <p>
     * Format: [orderBy]:[order]
     *
     * @param orderStr order string([orderBy]:[order])
     * @return order information
     */
    public static Order parseOrder(String orderStr) {
        return parseOrder(orderStr, DEFAULT_PARAM_SPLITTER);
    }

    /**
     * Parse order from string
     * <p>
     * Format: [orderBy][paramSplitter][order]
     *
     * @param orderStr order string([orderBy][paramSplitter][order])
     * @return order information
     */
    public static Order parseOrder(String orderStr, String paramSplitter) {
        if (Strings.isNullOrEmpty(orderStr)) {
            return null;
        }
        String[] orderValue = orderStr.split(paramSplitter);
        int order = ORDER_ASC;
        if (orderValue.length > 1) {
            order = Integer.parseInt(orderValue[1]);
        }
        return new Order(orderValue[0], order);
    }

    /**
     * Parse orders from string
     * <p>
     * Format: [orderBy1]:[order1],[orderBy2]:[order2]
     *
     * @param orderStr order string([orderBy1]:[order1],[orderBy2]:[order2])
     * @return orders information
     */
    public static List<Order> parseOrders(String orderStr) {
        return parseOrders(orderStr, DEFAULT_PARAM_SPLITTER, DEFAULT_ITEM_SPLITTER);
    }

    /**
     * Parse orders from string
     * <p>
     * Format: [orderBy1][paramSplitter][order1][itemSplitter][orderBy2][paramSplitter][order2]
     *
     * @param orderStr order string([orderBy1][paramSplitter][order1][itemSplitter][orderBy2][paramSplitter][order2])
     * @return orders information
     */
    public static List<Order> parseOrders(String orderStr, String paramSplitter, String itemSplitter) {
        if (Strings.isNullOrEmpty(orderStr)) {
            return null;
        }
        return Splitter.on(itemSplitter).splitToList(orderStr).stream().map(orderItemStr -> Order.parseOrder(orderItemStr, paramSplitter))
                .collect(Collectors.toList());
    }
}
