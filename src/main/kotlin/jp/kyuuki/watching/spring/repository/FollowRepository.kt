package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.Follow
import jp.kyuuki.watching.spring.model.User
import org.springframework.data.repository.CrudRepository

interface FollowRepository: CrudRepository<Follow, Int> {
    fun findByFromUserIdAndToUserId(fromUserId: Int, toUserId: Int):  List<Follow>
    fun findByFromUser(fromUser: User):  List<Follow>
    fun findByToUser(toUser: User):  List<Follow>
}