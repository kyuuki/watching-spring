package jp.kyuuki.watching.spring.integration.api

import com.fasterxml.jackson.databind.ObjectMapper
import jp.kyuuki.watching.spring.model.FollowRequest
import jp.kyuuki.watching.spring.model.User
import jp.kyuuki.watching.spring.repository.FollowRequestRepository
import jp.kyuuki.watching.spring.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
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
@Sql(statements = [
    "DELETE follow_requests;",
    "DELETE events;",
    "DELETE users;"
])
class FollowRequestIntegrationTests(@Autowired val mockMvc: MockMvc) {
    companion object {
        const val SENDER_API_KEY = "sender-api-key"

        val mapper = ObjectMapper()
    }

    @Autowired
    lateinit var followRequestRepository: FollowRequestRepository

    @Autowired
    lateinit var userRepository: UserRepository

    // データベース初期化
    // https://www.baeldung.com/spring-boot-data-sql-and-schema-sql

    // API 送信ユーザー
    lateinit var senderUser: User

    @BeforeEach
    fun setup() {
        println("setup")

        // ユーザー登録 (全ユーザーが消えている前提)
        senderUser = userRepository.save(User(phoneNumber = "+819099999999", apiKey = SENDER_API_KEY))
    }

    @Test
    fun testPostFollowRequests() {
        // フォローするユーザーをユーザー登録
        val followedUser = userRepository.save(User(phoneNumber = "+819099999991", apiKey = "apikey-from"))

        val requestBodyJson = mapper.writeValueAsString(
                mapOf("user_id" to followedUser.id))
        println(requestBodyJson)

        // API 実行
        val result = mockMvc.perform(post("/v1/follow_requests")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", SENDER_API_KEY)
                .content(requestBodyJson)
        )
                .andExpect(status().isOk)
                .andReturn()

        // データベース確認
        val followRequest = followRequestRepository.findAll().last()
        assertEquals(senderUser.id, followRequest.fromUser.id)
        assertEquals(followedUser.id, followRequest.toUser.id)

        // レスポンス確認
        assertEquals("", result.response.contentAsString)
    }

    @Test
    fun testPostFollowRequests_followToMySelf() {
        val requestBodyJson = mapper.writeValueAsString(
                mapOf("user_id" to senderUser.id))
        println(requestBodyJson)

        // API 実行
        val result = mockMvc.perform(post("/v1/follow_requests")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", SENDER_API_KEY)
                .content(requestBodyJson)
        )
                .andExpect(status().is5xxServerError)
                .andReturn()
    }

    @Test
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
                .header("x-api-key", SENDER_API_KEY)
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
