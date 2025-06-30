package com.ggomg.imagebff.task.domain

interface TaskEventPublisher {

    fun publishEnqueuedEvents(task: Task)
    
}