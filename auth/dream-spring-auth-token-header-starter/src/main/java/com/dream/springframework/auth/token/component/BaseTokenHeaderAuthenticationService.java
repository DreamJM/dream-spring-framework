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

package com.dream.springframework.auth.token.component;

import com.dream.springframework.auth.base.service.AuthenticationService;
import com.dream.springframework.auth.token.DreamTokenHeaderAuthProperties;
import com.dream.springframework.auth.token.exception.TokenException;
import com.dream.springframework.auth.token.exception.TokenExpiredException;
import com.dream.springframework.auth.token.model.TokenHeaderAuthUser;
import com.dream.springframework.base.exception.*;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token based user authentication service.
 * <p>
 * HTTP customized header used for conveying the token.
 *
 * @author DreamJM
 */
public abstract class BaseTokenHeaderAuthenticationService<T extends TokenHeaderAuthUser> implements AuthenticationService<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseTokenHeaderAuthenticationService.class);

    @Autowired
    private DreamTokenHeaderAuthProperties properties;

    /**
     * {@inheritDoc}
     */
    @Override
    public T authenticate(HttpServletRequest request, HttpServletResponse response) throws RequestException {
        String token = request.getHeader(properties.getAuthHeader());
        if (Strings.isNullOrEmpty(token)) {
            throw new UnauthorizedException(BaseErrorCode.AUTH_MISSING);
        }
        try {
            T user = parseToken(token);
            if (user.isNeedRefresh()) {
                refreshToken(response, user);
            }
            return user;
        } catch (TokenExpiredException ex) {
            throw new UnauthorizedException(BaseErrorCode.AUTH_EXPIRED, ex);
        } catch (TokenException ex) {
            throw new ForbiddenException(BaseErrorCode.TOKEN_CHECK_ERROR, ex);
        } catch (RequestException | InternalServerException ex) {
            throw ex;
        } catch (Throwable ex) {
            logger.error("Unexpected exception occurred!", ex);
            throw new UnauthorizedException(BaseErrorCode.AUTH_FAILURE, ex);
        }
    }

    /**
     * Refresh token for user
     *
     * @param response response to set token in header
     * @param user specified user
     * @throws RequestException Exception while refreshing token
     */
    public void refreshToken(HttpServletResponse response, T user) throws RequestException {
        String newToken = generateNewToken(user);
        if (!Strings.isNullOrEmpty(newToken)) {
            user.setToken(newToken);
            response.setHeader(properties.getAuthHeader(), newToken);
        }
    }

    /**
     * Parses token and Gets the authenticated user information
     *
     * @param token authentication token
     * @return user session information for authenticated user
     * @throws TokenException        common token exception
     * @throws TokenExpiredException token expired exception
     * @throws RequestException      other request exception
     */
    protected abstract T parseToken(String token) throws TokenException, TokenExpiredException, RequestException;

    /**
     * Generate new token for user
     *
     * @param user User information
     * @return Mew token
     * @throws RequestException Exception while generating token
     */
    protected abstract String generateNewToken(T user) throws RequestException;
}
