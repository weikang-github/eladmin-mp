/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.workflow.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.rest.AnonymousAccess;
import me.zhengjie.modules.workflow.domain.WorkflowDefinition;
import me.zhengjie.modules.workflow.service.WorkflowDefinitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 */
@Api(tags = "工作流：流程定义管理")
@RestController
@RequestMapping("/api/workflow-definitions")
@RequiredArgsConstructor
public class WorkflowDefinitionController {

    private final WorkflowDefinitionService workflowDefinitionService;

    @ApiOperation("保存/更新流程定义")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> saveDefinition(@Validated @RequestBody WorkflowDefinition definition) {
        workflowDefinitionService.saveWithVersioning(definition);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("获取最新版本流程定义")
    @GetMapping("/latest/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> getLatestVersion(@PathVariable Long id) {
        return new ResponseEntity<>(workflowDefinitionService.getLatestVersion(id), HttpStatus.OK);
    }

    @ApiOperation("获取流程定义版本历史")
    @GetMapping("/versions/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> getVersionHistory(@PathVariable Long id) {
        return new ResponseEntity<>(workflowDefinitionService.getVersionHistory(id), HttpStatus.OK);
    }

    @ApiOperation("删除流程定义")
    @DeleteMapping("/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> deleteDefinition(@PathVariable Long id) {
        workflowDefinitionService.deleteDefinition(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
