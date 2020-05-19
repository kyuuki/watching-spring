package jp.kyuuki.watching.spring.web.api.v1

import javassist.NotFoundException
import jp.kyuuki.watching.spring.model.FollowRequest
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.service.EventService
import jp.kyuuki.watching.spring.service.FollowRequestService
import jp.kyuuki.watching.spring.web.api.request.PostFollowRequests
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST

@RestController
class FollowRequestController: BaseController() {
    companion object {
        private val logger = LoggerFactory.getLogger(FollowRequestController::class.java)
    }

    @Autowired
    lateinit var followRequestService: FollowRequestService

    /**
     * フォローリクエスト検索 API.
     */
    @RequestMapping("/follow_requests", method = [ GET ])
    fun getFollowRequests(@RequestHeader(name = "x-api-key") apiKey: String): List<FollowRequest> {
        logger.info("getFollowRequests")

        returnErrorDebug()

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        return followRequestService.get(user)
    }

    /**
     * フォローリクエスト登録 API.
     */
    @RequestMapping("/follow_requests", method = [ POST ])
    fun postFollowRequests(@RequestHeader(name = "x-api-key") apiKey: String,
                           @RequestBody postFollowRequests: PostFollowRequests) {
        logger.info("postFollowRequests")
        logger.info("user_id = ${postFollowRequests.userId}")

        returnErrorDebug()

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        // TODO: エラー処理
        followRequestService.save(user, postFollowRequests.userId)
    }

    /**
     * フォローリクエスト承認 API.
     */
    @RequestMapping("/follow_requests/{id}/accept", method = [ POST ])
    fun postFollowRequestsAccept(@RequestHeader(name = "x-api-key") apiKey: String,
                                 @PathVariable("id") id: Int) {
        logger.info("postFollowRequestsAccept")
        logger.info("id = $id")

        returnErrorDebug()

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        // TODO: エラー処理
        followRequestService.accept(id)
    }

    /**
     * フォローリクエスト拒否 API.
     */
    @RequestMapping("/follow_requests/{id}/decline", method = [ POST ])
    fun postFollowRequestsDecline(@RequestHeader(name = "x-api-key") apiKey: String,
                                 @PathVariable("id") id: Int) {
        logger.info("postFollowRequestsDecline")
        logger.info("id = $id")

        returnErrorDebug()

        // 認証
        // TODO: エラーレスポンス要検討
        val user: User = authenticationComponent.authenticate(apiKey) ?: throw NotFoundException("Authentication error")

        // TODO: エラー処理
        followRequestService.decline(id)
    }
}