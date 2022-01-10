package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EventRepository: JpaRepository<Event, Int> {
    fun findByUserId(userId: Int): List<Event>

    // 関連するイベントを取得
    @Query("SELECT e FROM Event e where e.user in :users ORDER BY e.createdAt ASC")
    //fun findRelatedUserIdOrderByCreatedAt(@Param("users") users: List<User>, pageable: Pageable): List<Event>
    fun findRelatedUserIdOrderByCreatedAt(@Param("users") users: List<User>): List<Event>
}