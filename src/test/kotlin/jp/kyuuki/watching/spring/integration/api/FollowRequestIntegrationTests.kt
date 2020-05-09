package jp.kyuuki.watching.spring.integration.api

import com.fasterxml.jackson.databind.ObjectMapper
import jp.kyuuki.watching.spring.model.FollowRequest
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.FollowRequestRepository
import jp.kyuuki.watching.spring.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
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
class FollowRequestIntegrationTests(@Autowired val mockMvc: MockMvc) {
    val mapper = ObjectMapper();

    @Autowired
    lateinit var followRequestRepository: FollowRequestRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    @Sql(statements = [
        "DELETE event;",
        "DELETE user;",
        "INSERT INTO user (phone_number, api_key) VALUES ('+819099999999', 'xxxapikey');"
    ])
    fun testPostFollowRequestsDecline() {
        // ユーザー登録
        val fromUser = User(phoneNumber = "+819099999991", apiKey = "apikey-from")
        userRepository.save(fromUser)
        val toUser = User(phoneNumber = "+819099999992", apiKey = "apikey-to")
        userRepository.save(toUser)

        // フォローリクエスト登録
        val followRequest = followRequestRepository.save(
                FollowRequest(fromUser = fromUser, toUser = toUser))

        // API 実行
        val result = mockMvc.perform(post("/v1/follow_requests/${followRequest.id}/decline")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", "xxxapikey")
        )
                .andExpect(status().isOk)
                .andReturn()

        // データベース確認
        val count = followRequestRepository.findAll().count()
        assertEquals(0, count)

        // レスポンス確認
        assertEquals("", result.response.contentAsString)
    }
}
