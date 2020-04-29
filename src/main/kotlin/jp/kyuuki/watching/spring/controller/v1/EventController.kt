package jp.kyuuki.watching.spring.controller.v1

import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.service.AuthenticationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EventController: BaseController() {
    companion object {
        private val logger = LoggerFactory.getLogger(EventController::class.java)
    }

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @RequestMapping("/events", method = [ GET ])
    fun getEvents(): List<Event> {
        logger.info("getEvents")
        val userArray = arrayOf(User(2314, "", "イソップ", ""), User(1234, "", "サチ", ""))
        val descriptionArray = arrayOf("おはよう", "おやすみ")
        val list = mutableListOf<Event>()
        for (i in 1..30) {
            val user = userArray[i % userArray.size]
            val description = descriptionArray[i % descriptionArray.size]

            list.add(Event(i, description, Date(), user))
        }

        return list
    }
}