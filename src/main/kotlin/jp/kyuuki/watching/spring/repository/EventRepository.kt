package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository: JpaRepository<Event, Int> {
    fun findByUserId(userId: Int): List<Event>
}