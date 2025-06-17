package com.ggomg.imagebff.taskQueue.infrastructure

import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

@Component
class TaskDispatcher(
    private val taskQueue: InMemoryQueue,
    private val taskProcessor: TaskProcessor,
) {
    private val dispatcherThread = Thread(::dispatchLoop).apply {
        isDaemon = true
        start()
    }

    private val maxInFlight = 10 // 동시에 처리 가능한 최대 작업 수
    private val inFlight = AtomicInteger(0)

    private fun dispatchLoop() {
        while (true) {
            if (inFlight.get() >= maxInFlight) {
                Thread.sleep(100) // 유량제어: 과부하 상태 → 대기
                continue
            }

            val task = taskQueue.take() // 블로킹 대기
            inFlight.incrementAndGet()

            // 비동기 처리
            CompletableFuture.runAsync {
                try {
                    taskProcessor.process(task)
                    println("✅ 처리 완료: ${task.jobId}")
                } catch (e: Exception) {
                    println("❌ 처리 실패: ${task.jobId}")
                    // 재시도하거나 DLQ에 넣기 등 추가 로직 가능
                } finally {
                    inFlight.decrementAndGet()
                }
            }
        }
    }
}