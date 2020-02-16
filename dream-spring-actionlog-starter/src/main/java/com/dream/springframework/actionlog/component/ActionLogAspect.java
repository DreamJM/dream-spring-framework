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

package com.dream.springframework.actionlog.component;

import com.dream.springframework.actionlog.annotation.ActionLog;
import com.dream.springframework.actionlog.annotation.ActionLogHint;
import com.dream.springframework.actionlog.repository.ActionLogCreationRepo;
import com.dream.springframework.actionlog.repository.ActionLogReqEntity;
import com.dream.springframework.auth.base.BaseAuthUser;
import com.dream.springframework.base.util.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

/**
 * Aspect for action log
 * <p>
 * For method annotated with {@link ActionLog}, action information will be collected, serialized and stored automatically
 *
 * @author DreamJM
 */
@Aspect
public class ActionLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ActionLogAspect.class);

    private static final Pattern PATTERN_NUMBER = Pattern.compile("^[\\d]*$");

    private ActionLogCreationRepo repository;

    private Executor actionLogExecutor;

    private ObjectMapper mapper;

    private boolean i18nEnabled;

    private Locale locale;

    /**
     * @param repository        repository to store action log information
     * @param actionLogExecutor executor to store and serialize action log
     * @param mapper            jackson mapper for serialization
     * @param i18nEnabled       whether i18n enabled
     * @param locale            locale for i18n
     */
    public ActionLogAspect(ActionLogCreationRepo repository, Executor actionLogExecutor, ObjectMapper mapper, boolean i18nEnabled,
                           Locale locale) {
        this.repository = repository;
        this.actionLogExecutor = actionLogExecutor;
        this.mapper = mapper;
        this.i18nEnabled = i18nEnabled;
        this.locale = locale;
    }

    @Pointcut("@annotation(actionLog)")
    public void log(ActionLog actionLog) {
    }

    @Around(value = "log(actionLog)", argNames = "joinPoint,actionLog")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, ActionLog actionLog) throws Throwable {
        Object result = joinPoint.proceed();
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            BaseAuthUser authUser = (BaseAuthUser) request.getAttribute(BaseAuthUser.USER_KEY);
            if (authUser != null) {
                ActionLogReqEntity entity = new ActionLogReqEntity();
                entity.setOptrId(authUser.getUid());
                String clientIp = request.getHeader("x-forwarded-for");
                entity.setClientIp(clientIp == null ? request.getRemoteAddr() : clientIp);
                entity.setModuleId(actionLog.moduleId());
                entity.setFuncType(actionLog.funcType());
                StringBuilder sb = new StringBuilder();
                for (ActionLogHint hint : actionLog.hints()) {
                    try {
                        Object targetObj = hint.isReturn() ? result : joinPoint.getArgs()[hint.index()];
                        Object value = getFieldValue(targetObj, hint.value());
                        String hintContent;
                        if (value == null) {
                            hintContent = "";
                        } else if (value instanceof List) {
                            hintContent = Joiner.on("&").join((List<?>) value);
                        } else {
                            hintContent = value.toString();
                        }
                        if (sb.length() > 0) {
                            sb.append(',');
                        }
                        if (i18nEnabled) {
                            sb.append(MessageUtils.get(hint.name(), locale)).append(':').append(hintContent);
                        } else {
                            sb.append(hint.name()).append(':').append(hintContent);
                        }
                    } catch (Exception ex) {
                        logger.error("ActionLogHint annotation parse error!", ex);
                    }
                }
                entity.setHints(sb.toString());
                Map<String, Object> objs = new HashMap<>((int) (actionLog.values().length / 0.75 + 1));
                for (int i = 0; i < actionLog.values().length; i++) {
                    objs.put(actionLog.values()[i].key(),
                            actionLog.values()[i].isReturn() ? result : joinPoint.getArgs()[actionLog.values()[i].index()]);
                }
                writeActionLog(entity, objs);
            } else {
                logger.warn("Auth user not found, please check interceptor path or @AuthIgnore");
            }
        } else {
            logger.warn("Join Point not on the request thread: {}", Thread.currentThread().getName());
        }
        return result;
    }

    /**
     * Serializes and stores the action log entity
     *
     * @param entity    action log entity
     * @param detailObj action detail to be serialized
     */
    private void writeActionLog(ActionLogReqEntity entity, Map<String, Object> detailObj) {
        actionLogExecutor.execute(() -> {
            try {
                entity.setDetail(mapper.writeValueAsString(detailObj));
                repository.insert(entity);
            } catch (JsonProcessingException e) {
                logger.error("Serialization of action detail failed", e);
            }
        });
    }

    /**
     * Get object's specified field value recursively.
     * <p>
     * To access the field in side field, '.' could be used.
     * Example: field1.field2
     *
     * @param ob        target object
     * @param nameValue field name
     * @return field value
     * @throws InvocationTargetException Reflection Exception
     * @throws IllegalAccessException    Reflection Exception
     */
    private static Object getFieldValue(Object ob, String nameValue) throws InvocationTargetException, IllegalAccessException {
        if (Strings.isNullOrEmpty(nameValue)) {
            return ob;
        }
        String[] names = nameValue.split("[.]");
        Object targetObj = ob;
        for (String name : names) {
            if (ob instanceof List) {
                List<?> list = (List<?>) ob;
                if (PATTERN_NUMBER.matcher(name).matches()) {
                    int index = Integer.parseInt(name);
                    targetObj = list.get(index);
                } else {
                    targetObj = getCollectionFieldValues(list, name);
                }
            } else if (ob instanceof Collection) {
                Collection<?> collection = (Collection<?>) ob;
                if (PATTERN_NUMBER.matcher(name).matches()) {
                    int index = Integer.parseInt(name);
                    targetObj = collection.toArray()[index];
                } else {
                    targetObj = getCollectionFieldValues(collection, name);
                }
            } else if (ob.getClass().isArray()) {
                if (PATTERN_NUMBER.matcher(name).matches()) {
                    int index = Integer.parseInt(name);
                    targetObj = Array.get(ob, index);
                } else {
                    ArrayList<Object> tempObj = new ArrayList<>();
                    for (int i = 0; i < Array.getLength(ob); i++) {
                        Object itemValue = getDirectFieldValue(Array.get(ob, i), name);
                        if (itemValue != null) {
                            tempObj.add(itemValue);
                        }
                    }
                    targetObj = tempObj;
                }
            } else if (ob instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) ob;
                targetObj = map.get(name);
            } else {
                targetObj = getDirectFieldValue(targetObj, name);
            }
            if (targetObj == null) {
                return null;
            }
            if (targetObj instanceof List && ((List) targetObj).isEmpty()) {
                return null;
            }
        }
        return targetObj;
    }

    /**
     * Get collection's field values.
     *
     * @param collection data collection
     * @param name       field name
     * @return field value list
     * @throws InvocationTargetException Reflection Exception
     * @throws IllegalAccessException    Reflection Exception
     */
    private static List<Object> getCollectionFieldValues(Collection<?> collection, String name)
            throws InvocationTargetException, IllegalAccessException {
        ArrayList<Object> tempArray = new ArrayList<>();
        for (Object item : collection) {
            Object itemValue = getDirectFieldValue(item, name);
            if (itemValue != null) {
                tempArray.add(itemValue);
            }
        }
        return tempArray;
    }

    /**
     * Get object's specified field value.
     * <p>
     * The target field should have public getter ('getXxx') method (for boolean field type, 'isXxx' will be called)
     *
     * @param ob   target object
     * @param name field name
     * @return field value
     * @throws InvocationTargetException Reflection Exception
     * @throws IllegalAccessException    Reflection Exception
     */
    private static Object getDirectFieldValue(Object ob, String name) throws InvocationTargetException, IllegalAccessException {
        Field targetField = ReflectionUtils.findField(ob.getClass(), name);
        if (targetField == null) {
            logger.error("Target field {} not found on {}", name, ob.getClass().getTypeName());
            return null;
        }
        String methodName;
        if (targetField.getType() == boolean.class || targetField.getType() == Boolean.class) {
            methodName = "is" + capitalizeName(name);
        } else {
            methodName = "get" + capitalizeName(name);
        }
        Method method = ReflectionUtils.findMethod(ob.getClass(), methodName);
        if (method == null) {
            logger.error("Target method {} not found on {}", methodName, ob.getClass().getTypeName());
            return null;
        }
        return method.invoke(ob);
    }

    private static String capitalizeName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
