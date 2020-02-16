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

package com.dream.springframework.auth.base.configuration;

import com.dream.springframework.auth.base.resolver.OrgAuthCheckPathVariableResolver;
import com.dream.springframework.auth.base.resolver.OrgAuthCheckRequestParamResolver;
import com.dream.springframework.auth.base.service.AuthenticationService;
import com.dream.springframework.auth.base.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Argument resolver configuration
 *
 * @author DreamJM
 */
@ConditionalOnBean({AuthenticationService.class})
@Configuration
public class ArgumentResolverConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ArgumentResolverConfiguration.class);

    private RequestMappingHandlerAdapter adapter;

    private AuthorizationService service;

    private ConfigurableBeanFactory beanFactory;

    public ArgumentResolverConfiguration(RequestMappingHandlerAdapter adapter, AuthorizationService service,
                                         ConfigurableBeanFactory beanFactory) {
        this.adapter = adapter;
        this.service = service;
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void init() {
        try {
            // Replaces default PathVariableMethodArgumentResolver and RequestParamMethodArgumentResolver to handle organization
            // based authorization
            final List<HandlerMethodArgumentResolver> resolvers = Objects.requireNonNull(adapter.getArgumentResolvers()).stream()
                    .map(resolver -> {
                        if (PathVariableMethodArgumentResolver.class == resolver.getClass()) {
                            return new OrgAuthCheckPathVariableResolver(service);
                        } else if (RequestParamMethodArgumentResolver.class == resolver.getClass()) {
                            return new OrgAuthCheckRequestParamResolver(beanFactory, (RequestParamMethodArgumentResolver) resolver,
                                    service);
                        }
                        return resolver;
                    })
                    .collect(Collectors.toList());
            adapter.setArgumentResolvers(resolvers);
        } catch (NullPointerException e) {
            logger.error("Default Argument Resolver initialization error", e);
        }
    }

}
