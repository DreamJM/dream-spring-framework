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

package com.dream.springframework.demo.service;

import com.dream.springframework.base.exception.NotFoundException;
import com.dream.springframework.base.model.Id;
import com.dream.springframework.base.model.PageResult;
import com.dream.springframework.dao.util.PageUtil;
import com.dream.springframework.demo.constant.ErrorCode;
import com.dream.springframework.demo.domain.DemoRepository;
import com.dream.springframework.demo.domain.entity.DemoEntity;
import com.dream.springframework.demo.domain.query.DemoQuery;
import com.dream.springframework.demo.model.Demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author DreamJM
 */
@Service
public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    private DemoRepository repository;

    public DemoService(DemoRepository repository) {
        this.repository = repository;
    }

    @Async
    public void doSomethingAsync(long id) {
        logger.info(Thread.currentThread().getName());
    }

    public PageResult<Demo> query(DemoQuery order) {
        return PageUtil.parsePageResult(repository.query(order), Demo::new);
    }

    public Id insert(Demo demo) {
        return new Id(repository.insert(new DemoEntity(demo.getId(), demo.getName(), demo.getGreeting())));
    }

    public void update(Demo demo) throws NotFoundException {
        if (!repository.update(new DemoEntity(demo.getId(), demo.getName(), demo.getGreeting()))) {
            throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
        }
    }

    public Demo load(long id) throws NotFoundException {
        return new Demo(repository.load(id).orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND)));
    }

    public void delete(long id) throws NotFoundException {
        if (!repository.delete(id)) {
            throw new NotFoundException(ErrorCode.DATA_NOT_FOUND);
        }
    }
}
