package jp.kyuuki.watching.spring

import jp.kyuuki.watching.spring.model.Event
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/v1")
class RootController {
    companion object {
        private val logger = LoggerFactory.getLogger(RootController::class.java)
    }

    @RequestMapping("/users", method = [ POST ])
    fun postUsers(): Map<String, Any> {
        logger.info("postUsers")
        return mapOf("id" to 1234, "api_key" to "ABCDEFG")
    }

    @RequestMapping("/users", method = [ PUT ])
    fun putUsers(): Map<String, Int> {
        logger.info("putUsers")
        return mapOf()
    }

    @RequestMapping("/events", method = [ GET ])
    fun getEvents(): Array<Event> {
        logger.info("getEvents")
        return arrayOf(Event(1234, "おはよう"), Event(5678, "おやすみ"), Event(5678, "おはよう"))
    }
}