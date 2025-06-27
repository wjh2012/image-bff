package com.ggomg.imagebff.task.infrastructure.entity

import com.ggomg.imagebff.common.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tasks")
class TaskEntity(
    @Id
    var id: UUID,

    @Column
    val userId: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column
    var status: String,

    @Column
    val taskCreatedAt: LocalDateTime,

    @Column
    var taskModifiedAt: LocalDateTime,
) : BaseEntity()