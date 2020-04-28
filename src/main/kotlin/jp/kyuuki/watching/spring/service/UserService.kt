package jp.kyuuki.watching.spring.service

import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService() {
    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)

        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        const val STRING_LENGTH = 48
    }

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * ユーザー登録.
     */
    fun registor(phoneNumber: String): User {
        // 同じ電話番号のユーザーを探す
        var user = userRepository.findByPhoneNumber(phoneNumber)

        if (user == null) {
            user = User(phoneNumber = phoneNumber)

            // API キー作成
            // https://www.baeldung.com/kotlin-random-alphanumeric-string
            val randomString = (1..STRING_LENGTH)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("");

            user.apiKey = randomString

            userRepository.save(user)
        }

        logger.info(user.toString())

        return user
    }

    /**
     * ユーザー更新.
     */
    fun update(id: Int, nickname: String): User? {
        // 同じ電話番号のユーザーを探す
        var user = userRepository.findByIdOrNull(id)

        // TODO: 見つからなかった場合のエラー処理

        if (user != null) {
            user.nickname = nickname
            userRepository.save(user)
        }

        return user
    }
}