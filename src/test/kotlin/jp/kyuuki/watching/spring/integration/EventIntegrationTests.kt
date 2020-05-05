package jp.kyuuki.watching.spring.integration

import com.fasterxml.jackson.databind.ObjectMapper
import jp.kyuuki.watching.spring.repository.EventRepository
import jp.kyuuki.watching.spring.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class EventIntegrationTests(@Autowired val mockMvc: MockMvc) {
    val mapper = ObjectMapper();

    @Autowired
    lateinit var eventRepository: EventRepository

    @Test
    @Sql(statements = [
        "DELETE event;",
        "DELETE user;",
        "INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');"
    ])
    fun testGetEvents() {
        val result = mockMvc.perform(get("/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", "xxxapikey")
        )
                .andExpect(status().isOk)
                .andReturn()

        // TODO: レスポンス確認
        val node = mapper.readTree(result.response.contentAsString)
        println(node.size())
    }

    @Test
    @Sql(statements = [
        "DELETE event;",
        "DELETE user;",
        "INSERT INTO user (phone_number, nickname, api_key) VALUES ('+819099999999', 'Tokyo', 'xxxapikey');"
    ])
    fun testPostEvents() {
        val requestBodyJson = mapper.writeValueAsString(
                mapOf("description" to "Good morning"))
        println(requestBodyJson)

        val result = mockMvc.perform(post("/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", "xxxapikey")
                .content(requestBodyJson)
        )
                .andExpect(status().isOk)
                .andReturn()

        // データベース確認
        val event = eventRepository.findAll().last()
        println(event.toString())

        assertNotNull(event)
        assertEquals("Good morning", event!!.description)
        assertNotNull(event!!.createdAt)
        assertNotNull(event!!.user.id)

        // レスポンス確認
        val node = mapper.readTree(result.response.contentAsString)
        val user = node.get("user")
        println(node.get("id").asInt())
        println(node.get("description").asText())
        println(node.get("created_at").asText())

        assertNotNull(node.get("id").asInt())
        // TODO: なぜか日本語だと文字化けして正しい結果にならない
        assertEquals("Good morning", node.get("description").asText())
        assertNotNull(node.get("created_at").asText())
        assertNotNull(user.get("id").asInt())
        assertEquals("Tokyo", user.get("nickname").asText())
    }
}
