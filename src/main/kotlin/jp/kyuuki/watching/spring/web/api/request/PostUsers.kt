package jp.kyuuki.watching.spring.web.api.request

data class PostUsers(
        val phoneNumber: PhoneNumber
)

data class PhoneNumber(
        val countryCode: String,
        val original: String
)