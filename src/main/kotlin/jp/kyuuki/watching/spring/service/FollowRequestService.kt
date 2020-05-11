package jp.kyuuki.watching.spring.service

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.Follow
import jp.kyuuki.watching.spring.model.FollowRequest
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.FollowRepository
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
    lateinit var followRepository: FollowRepository

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
        // 自分自身へのリクエスト
        if (fromUser.id == toUserId) {
            // TODO: 例外の種類を要検討
            throw IllegalArgumentException("Cannot follow request to myself")
        }

        val toUser = userRepository.findByIdOrNull(toUserId)
        if (toUser == null) {
            // TODO: 例外の種類を要検討
            logger.error("Cannot find toUser")
            throw NotFoundException("Cannot find toUser")
        }

        // とりあえず、同じフォローリクエストはそのままスルー
        val existedRequests = followRequestRepository.findByFromUserIdAndToUserId(fromUser.id, toUserId)
        if (existedRequests.size == 1) {
            logger.debug("existedRequest = ${existedRequests[0]}")
            return existedRequests[0]
        } else if (existedRequests.size > 1)  {
            logger.error("Too many same follow requests")
            throw IllegalArgumentException("Too many same follow requests")
        }

        val followRequest = FollowRequest(
                fromUser = fromUser,
                toUser = toUser
        )
        followRequestRepository.save(followRequest)

        logger.info(followRequest.toString())

        return followRequest
    }

    /**
     * フォローリクエスト許可.
     */
    fun accept(id: Int) {
        // TODO: 他人のフォローリクエストを許可できないように

        val followRequest = followRequestRepository.findByIdOrNull(id)
        if (followRequest == null) {
            // TODO: 例外の種類を要検討
            logger.error("Cannot find followRequest")
            throw NotFoundException("Cannot find followRequest")
        }

        // TODO: トランザクションにする
        followRepository.save(Follow(fromUser = followRequest.fromUser, toUser = followRequest.toUser))
        followRequestRepository.deleteById(id)
    }

    /**
     * フォローリクエスト拒否.
     */
    fun decline(id: Int) {
        // TODO: 他人のフォローリクエストを拒否できないように

        // 将来的に拒否の履歴は残しておきたい

        followRequestRepository.deleteById(id)
    }
}
