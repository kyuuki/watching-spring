package jp.kyuuki.watching.spring.service

import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.google.i18n.phonenumbers.PhoneNumberUtil

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
     * ユーザー検索.
     */
    fun search(phoneNumber: String): User? {
        // 同じ電話番号のユーザーを探す
        var user = userRepository.findByPhoneNumber(phoneNumber)
        logger.info(user.toString())

        return user
    }

    /**
     * ユーザー登録.
     */
    fun registor(contryCode: String, original: String): User {
        // 電話番号正規化
        val formattedPhoneNumber = normalizePhoneNumber(contryCode, original)
        logger.debug("formattedPhoneNumber = $formattedPhoneNumber")

        // 同じ電話番号のユーザーを探す
        var user = userRepository.findByPhoneNumber(formattedPhoneNumber)

        if (user == null) {
            // API キー作成
            // https://www.baeldung.com/kotlin-random-alphanumeric-string
            val randomString = (1..STRING_LENGTH)
                    .map { kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("");

            user = User(phoneNumber = formattedPhoneNumber, apiKey = randomString)

            userRepository.save(user)
        }

        logger.info(user.toString())

        return user
    }

    /**
     * ユーザー更新.
     *
     * - 各項目が null の時は更新しない
     */
    fun update(id: Int, nickname: String?, fcmToken: String?): User? {
        // 同じ電話番号のユーザーを探す
        var user = userRepository.findByIdOrNull(id)

        // TODO: 見つからなかった場合のエラー処理

        if (user != null) {
            // safe call let
            nickname?.let { user.nickname = it }
            fcmToken?.let { user.fcmToken = it }
            userRepository.save(user)
        }

        return user
    }

    /**
     * 電話番号正規化.
     *
     * @return  正規化した電話番号 (ex. +819099999999)
     */
    fun normalizePhoneNumber(countryCode: String, original: String): String {
        val util: PhoneNumberUtil = PhoneNumberUtil.getInstance()
        val phoneNumber = util.parse(original, countryCode.toUpperCase())
                ?: throw IllegalArgumentException()

        return util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
    }
}