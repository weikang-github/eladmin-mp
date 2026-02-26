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
import me.zhengjie.modules.workflow.domain.WorkflowExecution;
import me.zhengjie.modules.workflow.service.WorkflowExecutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 */
@Api(tags = "工作流：流程执行管理")
@RestController
@RequestMapping("/api/workflow-executions")
@RequiredArgsConstructor
public class WorkflowExecutionController {

    private final WorkflowExecutionService workflowExecutionService;

    @ApiOperation("启动流程执行")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> startExecution(@RequestParam Long workflowId, 
                                                @RequestParam(required = false) String inputParams) {
        WorkflowExecution execution = workflowExecutionService.startExecution(workflowId, inputParams);
        return new ResponseEntity<>(execution.getId(), HttpStatus.CREATED);
    }

    @ApiOperation("获取流程执行状态")
    @GetMapping("/{id}/status")
    @AnonymousAccess
    public ResponseEntity<Object> getExecutionStatus(@PathVariable Long id) {
        return new ResponseEntity<>(workflowExecutionService.getExecutionStatus(id), HttpStatus.OK);
    }

    @ApiOperation("获取流程执行轨迹")
    @GetMapping("/{id}/trace")
    @AnonymousAccess
    public ResponseEntity<Object> getExecutionTrace(@PathVariable Long id) {
        return new ResponseEntity<>(workflowExecutionService.getExecutionTrace(id), HttpStatus.OK);
    }

    @ApiOperation("暂停流程执行")
    @PutMapping("/{id}/pause")
    @AnonymousAccess
    public ResponseEntity<Object> pauseExecution(@PathVariable Long id) {
        workflowExecutionService.pauseExecution(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("取消流程执行")
    @PutMapping("/{id}/cancel")
    @AnonymousAccess
    public ResponseEntity<Object> cancelExecution(@PathVariable Long id) {
        workflowExecutionService.cancelExecution(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
