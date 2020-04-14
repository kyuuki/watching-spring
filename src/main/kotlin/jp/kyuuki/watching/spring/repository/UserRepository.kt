package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Int> {
    fun findByPhoneNumber(phoneNumber: String): User?
    fun findByIdAndApiKey(id: Int, apiKey: String): User?
}