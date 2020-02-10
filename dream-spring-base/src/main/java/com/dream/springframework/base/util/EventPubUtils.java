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

import org.springframework.context.ApplicationEventPublisher;

/**
 * Spring Event Publisher
 *
 * <p>Provides static method to publish spring events
 *
 * @author DreamJM
 */
public class EventPubUtils {

    private static ApplicationEventPublisher publisher;

    /**
     * Called when spring context initialized
     *
     * @param publisher Spring Application Event Publisher to set
     */
    public static void setPublisher(ApplicationEventPublisher publisher) {
        EventPubUtils.publisher = publisher;
    }

    /**
     * Publishes spring event
     *
     * @param event event object
     */
    public static void publish(Object event) {
        publisher.publishEvent(event);
    }
}
