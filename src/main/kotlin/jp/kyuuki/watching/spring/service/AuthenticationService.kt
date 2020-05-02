package jp.kyuuki.watching.spring.service

import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthenticationService() {
    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)
    }

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * 認証.
     */
    fun authentication(apiKey: String): User? {
        return userRepository.findByApiKey(apiKey)
    }

    fun authentication(apiKey: String, id: Int): User? {
        return userRepository.findByIdAndApiKey(id, apiKey)
    }
}