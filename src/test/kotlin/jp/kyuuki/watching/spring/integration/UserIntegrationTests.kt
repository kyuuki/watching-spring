package jp.kyuuki.watching.spring.integration

import com.fasterxml.jackson.databind.ObjectMapper
import jp.kyuuki.watching.spring.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTests(@Autowired val mockMvc: MockMvc) {
    // https://www.baeldung.com/jackson-object-mapper-tutorial
    val mapper = ObjectMapper();

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    @Sql(statements = [
        "DELETE event;",
        "DELETE user;",
        "INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');",
        "INSERT INTO user (phone_number, api_key, nickname) VALUES ('+819011111111', '', 'Tokyo');"
    ])
    fun testGetUser() {
        val result = mockMvc.perform(get("/v1/users?phone_number=+819011111111")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", "xxxapikey")
        )
                .andExpect(status().isOk)
                .andReturn()

        // レスポンス確認
        val node = mapper.readTree(result.response.contentAsString)
        println(node.get("id").asInt())
        println(node.get("nickname").asText())

        assertNotNull(node.get("id").asInt())
        assertEquals("Tokyo", node.get("nickname").asText())
    }

    @Test
    @Sql(statements = ["DELETE user WHERE phone_number = '+819099999999';"])
    fun testPostUser() {
        val requestBodyJson = mapper.writeValueAsString(
                mapOf("phone_number" to mapOf(
                        "country_code" to "JP",
                        "original" to "09099999999")))
        println(requestBodyJson)

        val result = mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson)
        )
                .andExpect(status().isOk)
                .andReturn()

        // データベース確認
        // - JDBC で直接確認 (TODO)
        // - DBUnit を使って確認 (TODO)
        // - @DataJpaTest TestEntityManager を使ってテスト (TODO)
        // - Repository を使って確認
        val user = userRepository.findByPhoneNumber("+819099999999")
        println(user.toString())

        assertNotNull(user)
        assertNull(user!!.nickname)
        // 他の中身はレスポンスと照合

        // レスポンス確認
        val node = mapper.readTree(result.response.contentAsString)
        println(node.get("id").asInt())
        println(node.get("api_key").asText())

        assertEquals(user!!.id, node.get("id").asInt())
        assertEquals(user!!.apiKey, node.get("api_key").asText())
    }

    @Test
    @Sql(statements = [
        "DELETE event;",
        "DELETE user;",
        "INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');"
    ])
    fun testPutUser() {
        val requestBodyJson = mapper.writeValueAsString(mapOf("nickname" to "ベルリン"))
        println(requestBodyJson)

        val result = mockMvc.perform(put("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", "xxxapikey")
                .content(requestBodyJson)
        )
                .andExpect(status().isOk)
                .andReturn()

        // データベース確認
        val user = userRepository.findByApiKey("xxxapikey")
        println(user.toString())

        assertNotNull(user)
        assertEquals("ベルリン", user!!.nickname)

        // レスポンス確認
        assertEquals("", result.response.contentAsString)
    }
}
