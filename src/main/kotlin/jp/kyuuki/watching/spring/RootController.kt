package jp.kyuuki.watching.spring

import jp.kyuuki.watching.spring.model.Event
import jp.kyuuki.watching.spring.model.request.UpdateUser
import jp.kyuuki.watching.spring.model.request.UserRegistration
import jp.kyuuki.watching.spring.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/v1")
class RootController {
    companion object {
        private val logger = LoggerFactory.getLogger(RootController::class.java)
    }

    @Autowired
    lateinit var userService: UserService

    @RequestMapping("/users", method = [ POST ])
    fun postUsers(@RequestBody userRegistration: UserRegistration): Map<String, Any> {
        logger.info("postUsers")
        logger.info(userRegistration.toString())

        val user = userService.registor(userRegistration.phone_number)

        return mapOf("id" to user.id, "api_key" to user.apiKey)
    }

    @RequestMapping("/users", method = [ PUT ])
    fun putUsers(@RequestBody updateUser: UpdateUser): Map<String, Int> {
        logger.info("putUsers")
        logger.info(updateUser.toString())

        // TODO: 認証

        val user = userService.update(updateUser.id, updateUser.nickName)

        return mapOf()
    }

    @RequestMapping("/events", method = [ GET ])
    fun getEvents(): Array<Event> {
        logger.info("getEvents")
        return arrayOf(Event(1234, "おはよう"), Event(5678, "おやすみ"), Event(5678, "おはよう"))
    }
}