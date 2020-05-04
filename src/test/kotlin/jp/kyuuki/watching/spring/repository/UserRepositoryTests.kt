package jp.kyuuki.watching.spring.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class UserRepositoryTests {
    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    @Sql(statements = [
        "DELETE user;",
        "INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');"
    ])
    fun findByPhoneNumberTest() {
        val user = userRepository.findByPhoneNumber("+819099999999")
        println(user)

        assertEquals("+819099999999", user!!.phoneNumber)
        assertEquals("xxxapikey", user!!.apiKey)
    }
}