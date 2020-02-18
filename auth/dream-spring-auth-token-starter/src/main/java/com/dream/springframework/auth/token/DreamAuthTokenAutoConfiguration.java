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

package com.dream.springframework.auth.token;

import com.dream.springframework.auth.base.component.*;
import com.dream.springframework.auth.base.resolver.LoginUserHandlerMethodArgumentResolver;
import com.dream.springframework.auth.base.service.AuthorizationProvider;
import com.dream.springframework.auth.base.service.AuthorizationService;
import com.dream.springframework.auth.base.service.OrgPermissionService;
import com.dream.springframework.auth.token.component.BaseTokenAuthenticationService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Auto configuration for token based authorization service
 *
 * @author DreamJM
 */
@ConditionalOnBean(BaseTokenAuthenticationService.class)
@Configuration
@EnableConfigurationProperties(DreamTokenAuthProperties.class)
public class DreamAuthTokenAutoConfiguration implements WebMvcConfigurer {

    private DreamTokenAuthProperties properties;

    private BaseTokenAuthenticationService<?> authenticationService;

    /**
     * @param properties            token authorization properties
     * @param authenticationService token based authentication service
     */
    public DreamAuthTokenAutoConfiguration(DreamTokenAuthProperties properties, BaseTokenAuthenticationService<?> authenticationService) {
        this.properties = properties;
        this.authenticationService = authenticationService;
    }

    /**
     * @return user authorities and roles provider
     */
    @ConditionalOnMissingBean
    @Bean
    public AuthorizationProvider authorizationProvider() {
        return new DefaultAuthorizationProvider();
    }

    /**
     * @return authorization service for user authorities and roles
     */
    @ConditionalOnMissingBean
    @Bean
    public AuthorizationService authorizationService() {
        return new DefaultAuthorizationServiceImpl(properties, authorizationProvider());
    }

    /**
     * @param orgPermissionProvider ObjectProvider for organization resource access permission checking service
     * @param request               Http Servlet Request delegate
     * @return organization check advice
     */
    @Bean
    public OrgCheckAdvice orgCheckAdvice(ObjectProvider<OrgPermissionService<?>> orgPermissionProvider, HttpServletRequest request) {
        return new OrgCheckAdvice(orgPermissionProvider, authorizationService(), request);
    }

    /**
     * @param orgPermissionProvider ObjectProvider for organization resource access permission checking service
     * @param request               Http Servlet Request delegate
     * @return organization response check advice
     */
    @Bean
    public OrgRespCheckAdvice orgRespCheckAdvice(ObjectProvider<OrgPermissionService<?>> orgPermissionProvider,
                                                 HttpServletRequest request) {
        return new OrgRespCheckAdvice(orgPermissionProvider, authorizationService(), request);
    }

    /**
     * @return interceptor for authentication
     */
    @Bean
    public AuthenticationInterceptor authorizationInterceptor() {
        return new AuthenticationInterceptor(authenticationService, authorizationService());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns(properties.getPathPatterns())
                .excludePathPatterns(properties.getExcludePathPatterns());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
    }
}
