package jp.kyuuki.watching.spring.service

import jp.kyuuki.watching.spring.component.FcmComponent
import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.EventRepository
import jp.kyuuki.watching.spring.repository.FollowRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventService() {
    companion object {
        private val logger = LoggerFactory.getLogger(EventService::class.java)
    }

    @Autowired
    lateinit var eventRepository: EventRepository

    @Autowired
    lateinit var followRepository: FollowRepository

    @Autowired
    lateinit var fcmComponent: FcmComponent

    /**
     * イベント取得.
     */
    fun get(user: User): List<Event> {
        // TODO: あまりテストはしていない
        val relatedUsers = mutableListOf<User>(user)  // 自分自身は入れておく

        val followsFrom = followRepository.findByFromUser(user)
        val followsTo = followRepository.findByToUser(user)

        relatedUsers.addAll(followsFrom.map { it.toUser })
        relatedUsers.addAll(followsTo.map { it.fromUser })

        logger.debug(relatedUsers.toString())

        // 30 件にしぼる
        // TODO: API で設定できるように
        val paging: Pageable = PageRequest.of(1, 30)

        return eventRepository.findRelatedUserIdOrderByCreatedAt(relatedUsers, paging)
    }

    /**
     * イベント登録.
     */
    fun save(user: User, name: String): Event {
        val event = Event(
                name = name,
                createdAt = Date(),
                user = user
        )
        eventRepository.save(event)

        // TODO: エラー処理

        logger.info(event.toString())

        // FCM 通知
        // フォロー関係のユーザー取得
        val relatedUsers = mutableListOf<User>()

        val followsFrom = followRepository.findByFromUser(user)
        val followsTo = followRepository.findByToUser(user)

        relatedUsers.addAll(followsFrom.map { it.toUser })
        relatedUsers.addAll(followsTo.map { it.fromUser })

        logger.debug(relatedUsers.toString())

        fcmComponent.sendAddEventNotification(relatedUsers, event)

        return event
    }
}