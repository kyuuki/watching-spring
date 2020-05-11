package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.Follow
import org.springframework.data.repository.CrudRepository

interface FollowRepository: CrudRepository<Follow, Int> {
    fun findByFromUserIdAndToUserId(fromUserId: Int, toUserId: Int):  List<Follow>
}