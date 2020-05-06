package jp.kyuuki.watching.spring.web.api.v1

import jp.kyuuki.watching.spring.component.AuthenticationComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/v1")
open class BaseController {
    @Autowired
    lateinit var authenticationComponent: AuthenticationComponent
}