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

package com.dream.springframework.demo.controller;

import com.dream.springframework.auth.base.annotation.AuthIgnore;
import com.dream.springframework.base.annotation.AccessLimit;
import com.dream.springframework.base.exception.BaseErrorCode;
import com.dream.springframework.base.exception.NotFoundException;
import com.dream.springframework.base.exception.RequestException;
import com.dream.springframework.base.model.*;
import com.dream.springframework.base.util.BatchExecuteUtil;
import com.dream.springframework.dao.model.Order;
import com.dream.springframework.demo.domain.query.DemoQuery;
import com.dream.springframework.demo.model.Demo;
import com.dream.springframework.demo.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author DreamJM
 */
@Api(tags = "Demo API")
@RequestMapping("/api/demos")
@RestController
public class DemoController {

    private DemoService service;

    public DemoController(DemoService service) {
        this.service = service;
    }

    @AuthIgnore
    @ApiOperation("Query demos")
    @GetMapping
    public Result<PageResult<Demo>> queryDemos(@RequestParam(required = false) String keyword, @RequestParam int pageNum,
                                               @RequestParam int pageSize, @RequestParam(required = false) String orderBy) {
        return new Result<>(
                service.query(DemoQuery.builder().setKeyword(keyword).page(pageNum, pageSize).order(Order.parseOrder(orderBy)).build()));
    }

    @AuthIgnore
    @ApiOperation("Get detail of demo item")
    @GetMapping("{id}")
    public Result<Demo> getDemo(@PathVariable long id) throws NotFoundException {
        service.doSomethingAsync(id);
        return new Result<>(service.load(id));
    }

    @AuthIgnore
    @AccessLimit(key = "/api/demos", limit = 1)
    @ApiOperation("Create new demo item")
    @PostMapping
    public Result<Id> createDemo(@Validated @RequestBody Demo demo) throws RequestException, InterruptedException {
        Thread.sleep(5000);
        return new Result<>(service.insert(demo));
    }

    @AuthIgnore
    @ApiOperation("Update demo item")
    @PutMapping("{id}")
    public Success updateDemo(@PathVariable long id, @Validated @RequestBody Demo demo) throws NotFoundException {
        demo.setId(id);
        service.update(demo);
        return new Success();
    }

    @AuthIgnore
    @ApiOperation("Delete demo item")
    @DeleteMapping("{id}")
    public Success deleteDemo(@PathVariable long id) throws RequestException {
        service.delete(id);
        return new Success();
    }

    @AuthIgnore
    @ApiOperation("Batch delete demo items")
    @DeleteMapping
    public Result<List<ErrorItem>> batchDeleteDemos(@RequestParam Long[] ids) {
        Result<List<ErrorItem>> result = new Result<>(BatchExecuteUtil.executeBatch(Arrays.asList(ids), this::deleteDemo));
        if (result.getData() != null && result.getData().size() > 0) {
            result.setCode(BaseErrorCode.PART_ERROR);
        }
        return result;
    }

}
