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

package com.dream.springframework.actionlog.repository;

import java.util.Date;

/**
 * Action log entity
 *
 * @author DreamJM
 */
public class ActionLogReqEntity {

    /**
     * Operator identity
     */
    private String optrId;

    /**
     * Source client ip address
     */
    private String clientIp;

    /**
     * Module identity
     */
    private String moduleId;

    /**
     * Function type
     */
    private String funcType;

    /**
     * Action hints
     */
    private String hints;

    /**
     * Action detail json object
     */
    private String detail;

    /**
     * Action time
     */
    private Date actionTime;

    public ActionLogReqEntity() {
        actionTime = new Date();
    }

    public String getOptrId() {
        return optrId;
    }

    public void setOptrId(String optrId) {
        this.optrId = optrId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }
}
