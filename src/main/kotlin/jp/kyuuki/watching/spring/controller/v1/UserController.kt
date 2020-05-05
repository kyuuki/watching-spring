package jp.kyuuki.watching.spring.controller.v1

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.model.request.UpdateUser
import jp.kyuuki.watching.spring.model.request.UserRegistration
import jp.kyuuki.watching.spring.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.PUT

@RestController
class UserController: BaseController() {
    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }

    @Autowired
    lateinit var userService: UserService

    /**
     * ユーザー検索 API.
     */
    @RequestMapping("/users", method = [ GET ])
    fun getUsers(@RequestParam("phone_number") phoneNumber: String): User {
        logger.info("getUsers")
        logger.info("phone_number = $phoneNumber")

        // TODO: Validation

        val user = userService.search(phoneNumber)

        if (user == null) {
            // TODO: 404 を返す
            throw NotFoundException("Cannot find user")
        } else {
            return user
        }
    }

    /**
     * ユーザー登録 API.
     */
    @RequestMapping("/users", method = [ POST ])
    fun postUsers(@RequestBody userRegistration: UserRegistration): Map<String, Any> {
        logger.info("postUsers")
        logger.info(userRegistration.toString())

        val user = userService.registor(
                userRegistration.phoneNumber.countryCode,
                userRegistration.phoneNumber.original)

        return mapOf("id" to user.id, "api_key" to user.apiKey)
    }

    /**
     * ユーザー更新 API.
     */
    @RequestMapping("/users", method = [ PUT ])
    fun putUsers(@RequestHeader(name = "x-api-key") apiKey: String,
                 @RequestBody updateUser: UpdateUser) {
        logger.info("putUsers")
        logger.info("apiKey = $apiKey")
        logger.info(updateUser.toString())

        // 認証
        val user: User? = authenticationComponent.authenticate(apiKey)
        if (user == null) {
            // TODO: エラーレスポンス要検討
            throw NotFoundException("Authentication error")
        }

        userService.update(user.id, updateUser.nickname)
    }
}