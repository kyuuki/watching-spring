package jp.kyuuki.watching.spring.component

import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthenticationComponent() {
    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationComponent::class.java)
    }

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * 認証.
     */
    fun authenticate(apiKey: String): User? {
        return userRepository.findByApiKey(apiKey)
    }
}