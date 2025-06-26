package com.ggomg.imagebff.task.presentation

import com.ggomg.imagebff.auth.model.CustomUserDetails
import com.ggomg.imagebff.task.application.TaskService
import com.ggomg.imagebff.task.model.TaskCreateRequest
import com.ggomg.imagebff.task.model.TaskNameChangeRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Task", description = "작업 관리")
@RestController
@RequestMapping("/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping
    @Operation(summary = "작업 생성", description = "작업을 생성한다.")
    fun createTask(
        @AuthenticationPrincipal user: CustomUserDetails,
        @RequestBody request: TaskCreateRequest
    ) {
        taskService.save(userId = user.getId(), taskName = request.name)
    }

    @PatchMapping("/{taskId}")
    @Operation(summary = "작업명 수정", description = "작업 이름을 수정한다.")
    fun updateTaskName(
        @AuthenticationPrincipal user: CustomUserDetails,
        @PathVariable taskId: String,
        @RequestBody request: TaskNameChangeRequest
    ) {
        taskService.changeTaskName(
            userId = user.getId(),
            taskId = UUID.fromString(taskId),
            newName = request.name
        )
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "작업 삭제", description = "작업을 삭제한다.")
    fun deleteTask(
        @AuthenticationPrincipal user: CustomUserDetails,
        @PathVariable taskId: String,
    ) {
        taskService.delete(
            userId = user.getId(),
            taskId = UUID.fromString(taskId),
        )
    }
}