package jp.kyuuki.watching.spring.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTests(@Autowired val mockMvc: MockMvc) {
    @Test
    fun testPostUser() {
        val mapper = ObjectMapper();

        val result = mockMvc.perform(post("/v1/users")
                .contentType("application/json")
                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapOf("phone_number" to "09099999999"))))
                .andExpect(status().isOk)
                .andReturn()
        val node = mapper.readTree(result.response.contentAsString)

        println(node.get("id").asInt())
        println(node.get("api_key").asText())

        assertNotNull(node.get("id").asInt())
        assertNotNull(node.get("api_key").asText())
    }
}
