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
import com.dream.springframework.auth.token.exception.TokenException;
import com.dream.springframework.auth.token.exception.TokenExpiredException;
import com.dream.springframework.auth.token.model.TokenAuthUser;
import com.dream.springframework.base.exception.*;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Token based user authentication service.
 * <p>
 * HTTP Authorization header used for conveying the Bearer Token.
 * Example:
 * Authorization: Bearer xxxxxx
 *
 * @author DreamJM
 */
public abstract class BaseTokenAuthenticationService<T extends TokenAuthUser> implements AuthenticationService<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseTokenAuthenticationService.class);

    private static final String HEADER_AUTH = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public T authenticate(HttpServletRequest request) throws RequestException {
        String token = request.getHeader(HEADER_AUTH);
        if (Strings.isNullOrEmpty(token)) {
            throw new UnauthorizedException(BaseErrorCode.AUTH_MISSING);
        }
        if (!token.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException(BaseErrorCode.AUTH_FORMAT_ERROR);
        }
        token = token.substring(BEARER_PREFIX.length()).trim();
        try {
            return parseToken(token);
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
     * Parses token and Gets the authenticated user information
     *
     * @param token authentication token
     * @return user session information for authenticated user
     * @throws TokenException        common token exception
     * @throws TokenExpiredException token expired exception
     * @throws RequestException      other request exception
     */
    protected abstract T parseToken(String token) throws TokenException, TokenExpiredException, RequestException;
}
