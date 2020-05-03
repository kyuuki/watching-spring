package jp.kyuuki.watching.spring.model.request

data class UserRegistration(
        val phoneNumber: PhoneNumber
)

data class PhoneNumber(
        val countryCode: String,
        val original: String
)