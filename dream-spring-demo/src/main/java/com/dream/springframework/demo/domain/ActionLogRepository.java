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

package com.dream.springframework.demo.domain;

import com.dream.springframework.actionlog.repository.ActionLogCreationRepo;
import com.dream.springframework.actionlog.repository.ActionLogReqEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author DreamJM
 */
@Repository
public class ActionLogRepository implements ActionLogCreationRepo {

    private static final Logger logger = LoggerFactory.getLogger(ActionLogRepository.class);

    @Override
    public void insert(ActionLogReqEntity entity) {
        logger.info("New action log: {};{};{};{};{};{}", entity.getOptrId(), entity.getClientIp(), entity.getModuleId(),
                entity.getFuncType(), entity.getHints(), entity.getDetail());
    }
}
