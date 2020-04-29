package jp.kyuuki.watching.spring.controller.v1

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.request.UpdateUser
import jp.kyuuki.watching.spring.model.request.UserRegistration
import jp.kyuuki.watching.spring.service.AuthenticationService
import jp.kyuuki.watching.spring.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController: BaseController() {
    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authenticationService: AuthenticationService

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
    fun putUsers(@RequestHeader(name = "x-api-key") apiKey: String?,  // TODO: 後で必須に
                 @RequestBody updateUser: UpdateUser): Map<String, Int> {
        logger.info("putUsers")
        logger.info("apiKey = $apiKey")
        logger.info(updateUser.toString())

        // 認証
        if (apiKey != null) {  // TODO: 後で外す
            if (authenticationService.authentication(apiKey, updateUser.id) == null) {
                // TODO: エラーレスポンス要検討
                throw NotFoundException("Authentication error")
            }
        }

        val user = userService.update(updateUser.id, updateUser.nickname)

        return mapOf()
    }
}