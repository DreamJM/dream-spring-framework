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

import com.dream.springframework.dao.annotation.QueryCondition;
import com.dream.springframework.demo.domain.dao.DemoMapper;
import com.dream.springframework.demo.domain.entity.DemoEntity;
import com.dream.springframework.demo.domain.query.DemoQuery;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DreamJM
 */
@Repository
public class DemoRepository {

    private DemoMapper mapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DemoRepository(DemoMapper mapper) {
        this.mapper = mapper;
    }

    @QueryCondition
    public Page<DemoEntity> query(DemoQuery query) {
        return mapper.query(query);
    }

    public Optional<DemoEntity> load(long id) {
        return Optional.ofNullable(mapper.load(id));
    }

    public long insert(DemoEntity entity) {
        mapper.insert(entity);
        return entity.getId();
    }

    public boolean update(DemoEntity entity) {
        return mapper.update(entity);
    }

    public boolean delete(long id) {
        return mapper.delete(id);
    }
}
