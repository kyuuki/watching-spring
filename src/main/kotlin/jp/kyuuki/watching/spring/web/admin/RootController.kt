package jp.kyuuki.watching.spring.web.admin

import jp.kyuuki.watching.spring.repository.EventRepository
import jp.kyuuki.watching.spring.repository.UserRepository
import jp.kyuuki.watching.spring.service.EventService
import jp.kyuuki.watching.spring.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class RootController {
    companion object {
        private val logger = LoggerFactory.getLogger(RootController::class.java)
    }

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var eventRepository: EventRepository

    @RequestMapping("/")
    fun getIndex(model: Model): String {
        logger.info("getIndex")

        model.addAttribute("users", userRepository.findAll())
        model.addAttribute("events", eventRepository.findAll())

        return "admin/index"
    }
}