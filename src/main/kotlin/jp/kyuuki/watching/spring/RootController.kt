package jp.kyuuki.watching.spring

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
class RootController {

    @RequestMapping("/")
    fun root(): String {
        return "{ \"message\": \"Hello world\" }";
    }
}