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

/**
 * Consumer with specified exception
 *
 * @param <T> data type for consumer
 * @param <E> exception when handling
 * @author DreamJM
 */
@FunctionalInterface
public interface ExConsumer<T, E extends Throwable> {

    /**
     * Accepts data
     *
     * @param data data to accept
     * @throws E exception when handling
     */
    void accept(T data) throws E;

}
