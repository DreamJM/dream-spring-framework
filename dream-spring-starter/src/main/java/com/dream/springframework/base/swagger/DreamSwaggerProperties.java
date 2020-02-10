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

package com.dream.springframework.base.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author DreamJM
 */
@ConfigurationProperties(prefix = "dream.framework.swagger")
public class DreamSwaggerProperties {

    /**
     * Swagger title
     */
    private String title = "";

    /**
     * Swagger API version
     */
    private String version = "1.0";

    /**
     * Swagger annotation scan package
     */
    private String basePackage = "com.dream";

    /**
     * Ignored class types that will ignored in Swagger API
     */
    private Class<?>[] ignoredParameterTypes;

    /**
     * Whether Bearer Token is enabled
     */
    private boolean bearerTokenEnabled = true;

    /**
     * @return Swagger title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title Swagger title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return version of Swagger API document
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version version of Swagger API document to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return base package for swagger annotation scan
     */
    public String getBasePackage() {
        return basePackage;
    }

    /**
     * @param basePackage base package for swagger annotation scan
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * @return Ignored class types that will ignored in Swagger API
     */
    public Class<?>[] getIgnoredParameterTypes() {
        return ignoredParameterTypes;
    }

    /**
     * @param ignoredParameterTypes ignored class types that will ignored in Swagger API
     */
    public void setIgnoredParameterTypes(Class<?>[] ignoredParameterTypes) {
        this.ignoredParameterTypes = ignoredParameterTypes;
    }

    /**
     * @return Whether Bearer Token is enabled
     */
    public boolean isBearerTokenEnabled() {
        return bearerTokenEnabled;
    }

    /**
     * If true, Swagger api will add Authorization HTTP Header description
     *
     * @param bearerTokenEnabled {@code true} to enable the Bearer Token description
     */
    public void setBearerTokenEnabled(boolean bearerTokenEnabled) {
        this.bearerTokenEnabled = bearerTokenEnabled;
    }

}
