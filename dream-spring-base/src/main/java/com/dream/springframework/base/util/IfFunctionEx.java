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
 * {@link IfFunction} with exception
 *
 * @param <K> key for consumer
 * @param <V> data type for consumer
 * @param <E> exception type that consumer method throws
 * @author DreamJM
 * @see IfFunction
 */
public class IfFunctionEx<K, V, E extends Exception> {

    private Map<K, ExConsumer<V, E>> map = new HashMap<>();

    /**
     * Add data consumer for specified key
     *
     * @param key      key for data consumer
     * @param function data consumer
     * @return IfFunction object for chain
     */
    public IfFunctionEx<K, V, E> add(K key, ExConsumer<V, E> function) {
        this.map.put(key, function);
        return this;
    }

    /**
     * Provides value with specified key for data consumer to accept.
     *
     * @param key   key for data consumer
     * @param value data for consumer to accept
     * @throws E exception when handling
     */
    public void doIf(K key, V value) throws E {
        if (this.map.containsKey(key)) {
            map.get(key).accept(value);
        }
    }

    /**
     * Provides value with specified key for data consumer to accept. If consumer not found, then default consumer will be used
     *
     * @param key             key for data consumer
     * @param value           data for consumer to accept
     * @param defaultFunction default consumer if consumer not found with key
     * @throws E exception when handling
     */
    public void doIfWithDefault(K key, V value, Consumer<V> defaultFunction) throws E {
        if (this.map.containsKey(key)) {
            map.get(key).accept(value);
        } else {
            defaultFunction.accept(value);
        }
    }

}
