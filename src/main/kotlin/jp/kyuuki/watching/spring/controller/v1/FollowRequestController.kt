package jp.kyuuki.watching.spring.controller.v1

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.model.request.PostFollowRequests
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST

@RestController
class FollowRequestController: BaseController() {
    companion object {
        private val logger = LoggerFactory.getLogger(FollowRequestController::class.java)
    }

    /**
     * フォローリクエスト検索 API.
     */
    @RequestMapping("/follow_requests", method = [ GET ])
    fun getFollowRequests(@RequestHeader(name = "x-api-key") apiKey: String): List<Map<String, Any>> {
        logger.info("getFollowRequests")

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        // TODO

        return listOf(mapOf("id" to 3481, "from_user" to user))
    }

    /**
     * フォローリクエスト登録 API.
     */
    @RequestMapping("/follow_requests", method = [ POST ])
    fun postFollowRequests(@RequestHeader(name = "x-api-key") apiKey: String,
                           @RequestBody postFollowRequests: PostFollowRequests) {
        logger.info("postFollowRequests")
        logger.info("user_id = ${postFollowRequests.userId}")

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        // TODO
    }

    /**
     * フォローリクエスト承認 API.
     */
    @RequestMapping("/follow_requests/{id}/accept", method = [ POST ])
    fun postFollowRequestsAccept(@RequestHeader(name = "x-api-key") apiKey: String,
                                 @PathVariable("id") id: Int) {
        logger.info("postFollowRequestsAccept")
        logger.info("id = $id")

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        // TODO
    }
}