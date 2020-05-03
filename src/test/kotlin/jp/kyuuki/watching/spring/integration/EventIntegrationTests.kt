package jp.kyuuki.watching.spring.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class EventIntegrationTests(@Autowired val mockMvc: MockMvc) {
    val mapper = ObjectMapper();

    @Test
    @Sql(statements = ["INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');"])
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

        val node = mapper.readTree(result.response.contentAsString)
        println(node.get("id").asInt())
        println(node.get("description").asText())
        println(node.get("created_at").asText())

        // TODO: データベースに保存されていることを確認

        assertNotNull(node.get("id").asInt())
        // TODO: なぜか日本語だと文字化けして正しい結果にならない
        assertEquals("Good morning", node.get("description").asText())
        assertNotNull(node.get("created_at").asText())
    }
}
