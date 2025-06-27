package com.ggomg.imagebff.task.presentation

import com.ggomg.imagebff.auth.model.CustomUserDetails
import com.ggomg.imagebff.task.application.QueueService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "TaskQueue", description = "작업 큐 관리")
@RestController
@RequestMapping("/task")
class QueueController(
    private val queueService: QueueService
) {

    @PostMapping("/{taskId}/state/enqueue")
    @Operation(summary = "작업 큐 삽입", description = "작업을 큐에 적재한다.")
    fun enqueue(
        @AuthenticationPrincipal user: CustomUserDetails,
        @PathVariable taskId: String,
    ): ResponseEntity<Void> {
        queueService.enqueueTask(user.getId(), UUID.fromString(taskId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{taskId}/state/start")
    @Operation(summary = "작업 시작", description = "작업을 실행 상태로 전환한다.")
    fun start(
        @AuthenticationPrincipal user: CustomUserDetails,
        @PathVariable taskId: String,
    ): ResponseEntity<Void> {
        queueService.startTask(user.getId(), UUID.fromString(taskId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{taskId}/state/complete")
    @Operation(summary = "작업 완료", description = "작업을 완료 상태로 전환한다.")
    fun complete(
        @PathVariable taskId: String,
    ): ResponseEntity<Void> {
        queueService.completeTask(UUID.fromString(taskId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{taskId}/state/fail")
    @Operation(summary = "작업 실패", description = "작업을 실패 상태로 전환한다.")
    fun fail(
        @PathVariable taskId: String,
    ): ResponseEntity<Void> {
        queueService.failTask(UUID.fromString(taskId))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{taskId}/state/retry")
    @Operation(summary = "작업 재시도", description = "작업을 등록 상태로 되돌려 재시도한다.")
    fun retry(
        @AuthenticationPrincipal user: CustomUserDetails,
        @PathVariable taskId: String,
    ): ResponseEntity<Void> {
        queueService.retryTask(user.getId(), UUID.fromString(taskId))
        return ResponseEntity.ok().build()
    }
}