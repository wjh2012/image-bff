package com.ggomg.imagebff.taskQueue.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.PriorityBlockingQueue

/**
 * 작업 객체: 우선순위(priority)와 타임스탬프(timestamp)로 정렬됨
 */
data class Task(
    val userId: String,
    val jobId: String,
    var priority: Int,
    val timestamp: Long = System.currentTimeMillis()
) : Comparable<Task> {
    override fun compareTo(other: Task): Int {
        // priority 내림차순, 동일 우선순위 시 timestamp 오름차순
        val prioCmp = other.priority.compareTo(this.priority)
        return if (prioCmp != 0) prioCmp else this.timestamp.compareTo(other.timestamp)
    }
}

/**
 * JVM 메모리 기반 큐 컴포넌트
 */
@Component
class InMemoryQueue(
    // userId → 사용자별 Task 리스트
    private val userQueue: ConcurrentMap<String, MutableList<Task>>,
    // 전체 Task를 우선순위 기준으로 정렬하는 전역 큐
    private val globalQueue: PriorityBlockingQueue<Task>
) {
    // 사용자별 락 객체
    private val lockMap = ConcurrentHashMap<String, Any>()

    /** 작업 등록 */
    fun push(userId: String, jobId: String, priority: Int) {
        val task = Task(userId, jobId, priority)
        val list = userQueue.computeIfAbsent(userId) { mutableListOf() }
        synchronized(lockMap.computeIfAbsent(userId) { Any() }) {
            list.add(task)
        }
        globalQueue.offer(task)
    }

    /** 특정 작업 제거 */
    fun remove(userId: String, jobId: String): Boolean {
        val list = userQueue[userId] ?: return false
        synchronized(lockMap.computeIfAbsent(userId) { Any() }) {
            val removed = list.removeIf { it.jobId == jobId }
            if (removed) {
                globalQueue.removeIf { it.userId == userId && it.jobId == jobId }
            }
            return removed
        }
    }

    /** 우선순위 변경 */
    fun reorder(userId: String, jobId: String, newPriority: Int): Boolean {
        val list = userQueue[userId] ?: return false
        synchronized(lockMap.computeIfAbsent(userId) { Any() }) {
            val idx = list.indexOfFirst { it.jobId == jobId }
            if (idx == -1) return false
            val task = list[idx]
            globalQueue.remove(task)
            task.priority = newPriority
            globalQueue.offer(task)
            return true
        }
    }

    /** 사용자 큐 조회 */
    fun getUserQueue(userId: String): List<Task> {
        val list = userQueue[userId] ?: return emptyList()
        synchronized(lockMap.computeIfAbsent(userId) { Any() }) {
            return list.toList()
        }
    }

    /** 워커가 처리할 다음 Task 반환 */
    fun pollNext(): Task? = globalQueue.poll()

    fun take(): Task {
        return globalQueue.take() // 이건 진짜 block됨
    }
}

/**
 * 큐에 필요한 빈(bean) 정의: 애플리케이션 시작 시 단일 인스턴스 생성
 */
@Configuration
class QueueConfig {
    @Bean
    fun userQueue(): ConcurrentMap<String, MutableList<Task>> = ConcurrentHashMap()

    @Bean
    fun globalQueue(): PriorityBlockingQueue<Task> = PriorityBlockingQueue()
}
