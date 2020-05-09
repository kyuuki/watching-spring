package jp.kyuuki.watching.spring.repository

import jp.kyuuki.watching.spring.model.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class EventRepositoryTests {
    @Autowired
    lateinit var eventRepository: EventRepository

    //@Test
    @Sql(statements = [
        "DELETE user;",
        "INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');",
        "INSERT INTO event (description, user_id) VALUES ('おはよう', 1);",
        "INSERT INTO event (description, user_id) VALUES ('おはよう', 1);",
        "INSERT INTO event (description, user_id) VALUES ('おはよう', 1);"
    ])
    fun findByUserIdTest() {
        val events = eventRepository.findByUserId(1)
        println(events)
    }
}