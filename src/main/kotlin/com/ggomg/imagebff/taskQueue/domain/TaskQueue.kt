package com.ggomg.imagebff.taskQueue.domain

import java.util.UUID

class TaskQueue(
    val id: UUID,
    val entries: MutableList<QueueEntry> = mutableListOf(),
) {
    /** 새 엔트리를 맨 뒤에 추가 */
    fun enqueue(entry: QueueEntry) {
        // position 자동 부여
        val nextPos = if (entries.isEmpty()) 0 else entries.maxOf { it.position } + 1
        entry.moveTo(nextPos)
        entries.add(entry)
    }

    /**
     * 특정 태스크를 from → to 인덱스로 재배치
     * 내부 리스트에서 꺼내어 삽입하고, 전체 position을 0..n-1로 재동기화
     */
    fun reorder(taskId: UUID, toIndex: Int) {
        val idx = entries.indexOfFirst { it.taskId == taskId }
        require(idx != -1) { "큐에 해당 Task($taskId)가 없습니다." }
        require(toIndex in 0 until entries.size) { "toIndex($toIndex) 범위는 0..${entries.lastIndex} 여야 합니다." }

        val entry = entries.removeAt(idx)
        entries.add(toIndex, entry)

        // 전체 포지션 재할당
        entries.forEachIndexed { i, e -> e.moveTo(i) }
    }

    /** 맨 앞 항목을 꺼내고, 남은 항목들 포지션 재할당 */
    fun pollNext(): QueueEntry? {
        if (entries.isEmpty()) return null
        val next = entries.removeAt(0)
        entries.forEachIndexed { i, e -> e.moveTo(i) }
        return next
    }

    /** 현재 순서대로 조회할 때 사용 */
    fun list(): List<QueueEntry> = entries.sortedBy { it.position }
}