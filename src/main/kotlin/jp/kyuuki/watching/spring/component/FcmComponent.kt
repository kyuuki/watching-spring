package jp.kyuuki.watching.spring.component

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.MulticastMessage
import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat

@Component
class FcmComponent() {
    companion object {
        private val logger = LoggerFactory.getLogger(FcmComponent::class.java)
        private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    }

    // 失敗してもログに出力するのみ
    fun sendAddEventNotification(toUsers: List<User>, event: Event) {
        val registrationTokens = toUsers.mapNotNull { it.fcmToken }

        if (registrationTokens.isEmpty()) {
            logger.info("No registrationToken for user = $toUsers")
            return
        }

        val message = MulticastMessage.builder()
                .putData("id", event.id.toString())
                .putData("name", event.name)
                .putData("created_at", sdf.format(event.createdAt))
                .putData("user_id", event.user.id.toString())
                .putData("user_nickname", event.user.nickname)
                .addAllTokens(registrationTokens)
                .build()

        try {
            val response = FirebaseMessaging.getInstance().sendMulticast(message)
            logger.debug("Successfully sent message: $response")
        } catch (e: FirebaseMessagingException) {
            logger.error("Fcm error e.message = ${e.message}")
        }
    }
}