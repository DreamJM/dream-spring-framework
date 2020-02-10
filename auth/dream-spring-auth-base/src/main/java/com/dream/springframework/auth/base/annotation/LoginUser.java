/*
 * 文 件 名: LoginUser.java
 * 版    权: 南京集群软件技术有限公司. Copyright 2014-2020,  All rights reserved
 * 描    述:
 * 版    本：1.0
 * 创 建 人: JiangMeng
 * 创建时间: 2019/7/18
 */
package com.dream.springframework.auth.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses on the request mapping method's parameter with {@link com.dream.springframework.auth.base.BaseAuthUser BaseAuthUser} class type
 * or <b>subclass</b> type to indicate that the {@link com.dream.springframework.auth.base.BaseAuthUser BaseAuthUser} will be injected
 * automatically according to authorization result.
 * <p>
 * Example:
 * <pre class='code'>
 * {@code @PostMapping}
 * public void createDemo(@LoginUser BaseAuthUser, @RequestBody Demo demo) throws RequestException, InterruptedException {
 *     .......
 * }
 * </pre>
 *
 * @author DreamJM
 * @see com.dream.springframework.auth.base.BaseAuthUser
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
