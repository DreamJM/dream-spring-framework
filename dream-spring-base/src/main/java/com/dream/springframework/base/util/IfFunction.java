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

package com.dream.springframework.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Combines with lambda expression to substitute 'if else' or 'switch case'.
 * <p>Example:
 * <pre class="code">
 * IfFunction&lt;Integer, String&gt; func = new IfFunction&lt;&gt;();
 * func.add(1, System.out::println).add(2, this::doSomething);
 * func.doIf(1, "hello");
 * func.doIf(2, "hello");
 * </pre>
 *
 * @param <K> key for consumer
 * @param <V> data type for consumer
 * @author DreamJM
 */
public class IfFunction<K, V> {

    private Map<K, Consumer<V>> map = new HashMap<>();

    /**
     * Add data consumer for specified key
     *
     * @param key      key for data consumer
     * @param function data consumer
     * @return IfFunction object for chain
     */
    public IfFunction<K, V> add(K key, Consumer<V> function) {
        map.put(key, function);
        return this;
    }

    /**
     * Provides value with specified key for data consumer to accept.
     *
     * @param key   key for data consumer
     * @param value data for consumer to accept
     */
    public void doIf(K key, V value) {
        Consumer<V> consumer = map.get(key);
        if (consumer != null) {
            consumer.accept(value);
        }
    }

    /**
     * Provides value with specified key for data consumer to accept. If consumer not found, then default consumer will be used
     *
     * @param key             key for data consumer
     * @param value           data for consumer to accept
     * @param defaultFunction default consumer if consumer not found with key
     */
    public void doIfWithDefault(K key, V value, Consumer<V> defaultFunction) {
        map.getOrDefault(key, defaultFunction).accept(value);
    }

}
