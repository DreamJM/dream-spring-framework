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

package com.dream.springframework.demo.domain.query;

import com.dream.springframework.dao.model.BaseCondition;
import com.dream.springframework.dao.model.BaseQueryBuilder;
import com.dream.springframework.dao.model.Order;
import com.dream.springframework.dao.model.OrderColumn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DreamJM
 */
public class DemoQuery extends BaseCondition {

    private String keyword;

    private DemoQuery() {

    }

    public String getKeyword() {
        return keyword;
    }

    public static Builder builder() {
        return new Builder(new DemoQuery());
    }

    public static class Builder extends BaseQueryBuilder<DemoQuery> {

        private static final Order DEFAULT_ORDER = new Order("id", Order.ORDER_ASC);

        private static final Map<String, OrderColumn[]> ORDER_COLUMN_MAP =
                Collections.unmodifiableMap(new HashMap<String, OrderColumn[]>() {
                    {
                        put("id", new OrderColumn[]{new OrderColumn("id")});
                        put("name", new OrderColumn[]{new OrderColumn("name"), new OrderColumn("id", true)});
                    }
                });

        private Builder(DemoQuery query) {
            super(query);
        }

        public Builder setKeyword(String keyword) {
            query.keyword = keyword;
            return this;
        }

        public Builder page(int pageNum, int pageSize) {
            setPage(pageNum, pageSize);
            return this;
        }

        public Builder order(Order order) {
            buildOrder(order);
            return this;
        }

        @Override
        protected Map<String, OrderColumn[]> getOrderColumnMap() {
            return ORDER_COLUMN_MAP;
        }

        @Override
        protected Order getDefaultOrder() {
            return DEFAULT_ORDER;
        }
    }
}
