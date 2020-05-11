package jp.kyuuki.watching.spring.web.api.v1

import jp.kyuuki.watching.spring.component.AuthenticationComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/v1")
open class BaseController {
    @Autowired
    lateinit var authenticationComponent: AuthenticationComponent

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): Map<String, String> {
        return mapOf("message" to e.localizedMessage)
    }
}