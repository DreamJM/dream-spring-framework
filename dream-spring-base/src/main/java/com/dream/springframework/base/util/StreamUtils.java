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

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Stream related utils
 *
 * @author DreamJM
 */
public class StreamUtils {

    /**
     * If collection is {@code null}, return empty stream.
     *
     * @param collection collection to be converted to stream
     * @param <T>        collection data type
     * @return data stream
     */
    public static <T> Stream<T> convertNullStream(Collection<T> collection) {
        return collection == null ? Stream.empty() : collection.stream();
    }

    /**
     * Whether collection is null or empty
     *
     * @param collection collection
     * @return true if collection is null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
