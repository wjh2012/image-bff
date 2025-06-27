package com.ggomg.imagebff.task.domain

import com.fasterxml.uuid.Generators
import java.time.LocalDateTime
import java.util.*

class Task(
    private val id: UUID,
    private val userId: UUID,
    private var name: String,
    private var status: TaskState = RegisteredState,
    private val createdAt: LocalDateTime,
    private var updatedAt: LocalDateTime,
    private val taskImages: MutableList<TaskImage> = mutableListOf()
) {

    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "이름은 비어 있을 수 없습니다." }
        this.name = newName
        this.updatedAt = LocalDateTime.now()
    }

    fun addImage(imageId: UUID) {
        if (taskImages.any { it.imageId == imageId }) {
            throw IllegalArgumentException("이미 추가된 이미지입니다.")
        }
        val generatedUUID = Generators.timeBasedEpochGenerator().generate()
        taskImages.add(TaskImage(generatedUUID, id, imageId))
        this.updatedAt = LocalDateTime.now()
    }

    fun removeImage(taskImageId: UUID) {
        val removed = taskImages.removeIf { it.id == taskImageId }
        if (!removed) throw IllegalArgumentException("해당 이미지 연결을 찾을 수 없습니다.")
        this.updatedAt = LocalDateTime.now()
    }

    fun getId(): UUID = id
    fun getUserId(): UUID = userId
    fun getName(): String = name
    fun getCreatedAt(): LocalDateTime = createdAt
    fun getUpdatedAt(): LocalDateTime = updatedAt
    fun getTaskImages(): List<TaskImage> = taskImages.toList()
    fun getStatus(): TaskState = status

    // 상태 전이 메서드
    internal fun transitionTo(newState: TaskState) {
        this.status = newState
        this.updatedAt = LocalDateTime.now()
    }

    fun enqueue() {
        status.enqueue(this)
        updatedAt = LocalDateTime.now()
    }

    fun start() {
        status.start(this)
        updatedAt = LocalDateTime.now()
    }

    fun complete() {
        status.complete(this)
        updatedAt = LocalDateTime.now()
    }

    fun fail() {
        status.fail(this)
        updatedAt = LocalDateTime.now()
    }

    fun retry() {
        status.retry(this)
        updatedAt = LocalDateTime.now()

    }
}