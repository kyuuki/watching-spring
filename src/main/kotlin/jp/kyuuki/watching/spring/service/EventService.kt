package jp.kyuuki.watching.spring.service

import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.google.i18n.phonenumbers.PhoneNumberUtil
import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.repository.EventRepository
import java.util.*

@Service
class EventService() {
    companion object {
        private val logger = LoggerFactory.getLogger(EventService::class.java)
    }

    @Autowired
    lateinit var eventRepository: EventRepository

    /**
     * イベント登録.
     */
    fun save(user: User, description: String): Event {
        val event = Event(
                description = description,
                createdAt = Date(),
                user = user
        )
        eventRepository.save(event)

        // TODO: エラー処理

        logger.info(event.toString())

        return event
    }
}