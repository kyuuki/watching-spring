package jp.kyuuki.watching.spring.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserRepositoryTests {
    @Autowired
    lateinit var userRepository: UserRepository

    //@Test
    fun findByPhoneNumberTest() {
        var a = userRepository.findByPhoneNumber("xxxx")
        println(a)
        assertEquals(null, a)
        assertEquals(1, 1)
    }
}