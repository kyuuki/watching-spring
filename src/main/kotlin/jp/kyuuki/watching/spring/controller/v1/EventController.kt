package jp.kyuuki.watching.spring.controller.v1

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.model.request.PostEvents
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EventController: BaseController() {
    companion object {
        private val logger = LoggerFactory.getLogger(EventController::class.java)
    }

    @RequestMapping("/events", method = [ GET ])
    fun getEvents(@RequestHeader(name = "x-api-key") apiKey: String): List<Event> {
        logger.info("getEvents")
        logger.info("apiKey = $apiKey")

        // 認証
        val user: User? = authenticationComponent.authenticate(apiKey)
        if (user == null) {
            // TODO: エラーレスポンス要検討
            throw NotFoundException("Authentication error")
        }

        val userArray = arrayOf(User(2314, "", "イソップ", ""), User(1234, "", "サチ", ""))
        val descriptionArray = arrayOf("おはよう", "おやすみ")
        val list = mutableListOf<Event>()

        // TOOD: デバッグ用に 1 つめのメッセージは認証情報
        list.add(Event(1001, "送られた x-api-key は $apiKey だよ！", Date(), user))
        list.add(Event(1002, "あなたのニックネームは ${user.nickname}", Date(), user))
        list.add(Event(1003, "あなたの ID は ${user.id}", Date(), user))
        list.add(Event(1004, "あなたの電話番号は ${user.phoneNumber}", Date(), user))

        for (i in 1..30) {
            val user = userArray[i % userArray.size]
            val description = descriptionArray[i % descriptionArray.size]

            list.add(Event(i, description, Date(), user))
        }

        return list
    }

    @RequestMapping("/events", method = [ POST ])
    fun postEvents(@RequestHeader(name = "x-api-key") apiKey: String,
                   @RequestBody postEvents: PostEvents): Event {
        logger.info("postEvents")
        logger.info("apiKey = $apiKey")
        logger.info("description = ${postEvents.description}")

        // 認証
        val user: User? = authenticationComponent.authenticate(apiKey)
        if (user == null) {
            // TODO: エラーレスポンス要検討
            throw NotFoundException("Authentication error")
        }

        return Event(1001, postEvents.description, Date(), user)
    }
}