package jp.kyuuki.watching.spring.web.api.v1

import javassist.NotFoundException
import jp.kyuuki.watching.spring.component.AuthenticationComponent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.IllegalStateException
import kotlin.random.Random

@RequestMapping("/v1")
open class BaseController {
    companion object {
        private val logger = LoggerFactory.getLogger(BaseController::class.java)
    }

    @Autowired
    lateinit var authenticationComponent: AuthenticationComponent

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleException(e: NotFoundException): Map<String, String> {
        return mapOf("message" to e.localizedMessage)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): Map<String, String> {
        return mapOf("message" to e.localizedMessage)
    }

    // デバッグ用に通常と異なるレスポンスを返す
    fun returnErrorDebug() {
        return

        val randomInt = Random.nextInt(100)

        // 10 回に 1 回 10 秒待ち
        if (randomInt % 10 == 0) {
            logger.debug("Sleep")
            Thread.sleep(10000)
        }

        // 3 回に 1 回ぐらいエラー
        if (randomInt % 3 == 0) {
            logger.debug("Debug error")
            throw IllegalStateException("Debug error")
        }
    }
}