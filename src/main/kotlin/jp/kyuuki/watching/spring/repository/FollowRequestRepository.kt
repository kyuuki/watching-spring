package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.FollowRequest
import jp.kyuuki.watching.spring.model.User
import org.springframework.data.repository.CrudRepository

interface FollowRequestRepository: CrudRepository<FollowRequest, Int> {
    fun findByToUserId(toUserId: Int): List<FollowRequest>
}