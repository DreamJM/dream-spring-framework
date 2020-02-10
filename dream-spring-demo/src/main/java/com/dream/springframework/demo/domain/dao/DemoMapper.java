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

package com.dream.springframework.demo.domain.dao;

import com.dream.springframework.demo.domain.entity.DemoEntity;
import com.dream.springframework.demo.domain.query.DemoQuery;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

/**
 * @author DreamJM
 */
@Mapper
public interface DemoMapper {

    @Select("<script>SELECT id,name,greeting FROM demo WHERE 1=1 " +
            "<if test='keyword != null'>AND (name LIKE CONCAT('%',#{keyword},'%') OR greeting LIKE CONCAT('%',#{keyword},'%'))</if>" +
            "</script>")
    Page<DemoEntity> query(DemoQuery query);

    @Select("SELECT id,name,greeting FROM demo WHERE id=#{param1}")
    DemoEntity load(long id);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO demo(name,greeting) VALUES(#{name},#{greeting})")
    long insert(DemoEntity entity);

    @Update("UPDATE demo SET name=#{name},greeting=#{greeting} WHERE id=#{id}")
    boolean update(DemoEntity entity);

    @Delete("DELETE FROM demo WHERE id=#{param1}")
    boolean delete(long id);
}
