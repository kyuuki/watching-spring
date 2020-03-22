package jp.kyuuki.watching.spring.repotitory

import jp.kyuuki.watching.spring.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Int> {
    fun findByPhoneNumber(phoneNumber: String): User?
}