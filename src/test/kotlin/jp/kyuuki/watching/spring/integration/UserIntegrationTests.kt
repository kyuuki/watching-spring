package jp.kyuuki.watching.spring.integration

import com.fasterxml.jackson.databind.ObjectMapper
import jp.kyuuki.watching.spring.model.User
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
class UserIntegrationTests(@Autowired val mockMvc: MockMvc) {
    // https://www.baeldung.com/jackson-object-mapper-tutorial
    val mapper = ObjectMapper();

    @Test
    @Transactional
    fun testPostUser() {
        val requestBodyJson = mapper.writeValueAsString(mapOf("phone_number" to "09099999999"))
//        val requestBodyJson = mapper.writeValueAsString(
//                mapOf("phone_number" to mapOf(
//                        "country_code" to 81,
//                        "original" to "09099999999")))
        println(requestBodyJson)

        val result = mockMvc.perform(post("/v1/users")
                .contentType("application/json")
                .content(requestBodyJson))

                .andExpect(status().isOk)
                .andReturn()

        val node = mapper.readTree(result.response.contentAsString)
        println(node.get("id").asInt())
        println(node.get("api_key").asText())

        assertNotNull(node.get("id").asInt())
        assertNotNull(node.get("api_key").asText())
    }

    @Test
    fun testPutUser() {
        // TODO: データベースに API キー登録

        val result = mockMvc.perform(put("/v1/users")
                .contentType("application/json")
                //.header("x-api-key", "xxxxxx")
                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapOf("id" to "1", "nick_name" to "イソップ"))))
                .andExpect(status().isOk)
                .andReturn()
        val node = mapper.readTree(result.response.contentAsString)

        // TODO: キーがないこと
    }
}
