package jp.kyuuki.watching.spring.service

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.FollowRequest
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.FollowRequestRepository
import jp.kyuuki.watching.spring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FollowRequestService() {
    companion object {
        private val logger = LoggerFactory.getLogger(FollowRequestService::class.java)
    }

    @Autowired
    lateinit var followRequestRepository: FollowRequestRepository

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * フォローリクエスト取得.
     */
    fun get(user: User): List<FollowRequest> {
        return followRequestRepository.findByToUserId(user.id)
    }

    /**
     * フォローリクエスト登録.
     */
    fun save(fromUser: User, toUserId: Int): FollowRequest {
        val toUser = userRepository.findByIdOrNull(toUserId)

        // TODO: 自分自身へのリクエスト

        // TODO: 複数回のフォローリクエスト (DB に制約？)

        // TODO: 見つからなかった場合
        if (toUser == null) {
            throw NotFoundException("Cannot find toUser")
        }

        val followRequest = FollowRequest(
                fromUser = fromUser,
                toUser = toUser
        )
        followRequestRepository.save(followRequest)

        // TODO: エラー処理

        logger.info(followRequest.toString())

        return followRequest
    }

    /**
     * フォローリクエスト許可.
     */
    fun accept(id: Int) {
        // TODO: トランザクションにする
        // TODO: フォローテーブル挿入
        // TODO: 他人のフォローリクエストを許可できないように

        followRequestRepository.deleteById(id)
    }
}
