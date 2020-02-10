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

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Swagger Api configuration based on {@link DreamSwaggerProperties swagger properties}
 *
 * <p>Active when 'dev' profile
 *
 * @author DreamJM
 */
@EnableSwagger2
@Configuration
@Profile("dev")
@EnableConfigurationProperties(DreamSwaggerProperties.class)
public class DreamSwaggerAutoConfiguration {

    private DreamSwaggerProperties properties;

    public DreamSwaggerAutoConfiguration(DreamSwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title(properties.getTitle()).version(properties.getVersion()).build()).select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage())).paths(PathSelectors.any()).build();
        if (properties.getIgnoredParameterTypes() != null) {
            docket.ignoredParameterTypes(properties.getIgnoredParameterTypes());
        }
        if (properties.isBearerTokenEnabled()) {
            docket.globalOperationParameters(Collections.singletonList(
                    new ParameterBuilder().name("Authorization").description("Authorization Bearer Token").modelRef(new ModelRef("string"))
                            .parameterType("header").required(false).build()));
        }
        return docket;
    }

}
