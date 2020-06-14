package jp.kyuuki.watching.spring.web.api.request

data class PutUsers(
        val nickname: String?,
        val fcmToken: String?
)