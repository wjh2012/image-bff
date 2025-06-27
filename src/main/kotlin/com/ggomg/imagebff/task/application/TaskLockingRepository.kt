package com.ggomg.imagebff.task.application

import com.ggomg.imagebff.task.domain.Task
import java.util.UUID

interface TaskLockingRepository {
    fun findByUserIdAndIdForUpdate(userId: UUID, id: UUID): Task?
}