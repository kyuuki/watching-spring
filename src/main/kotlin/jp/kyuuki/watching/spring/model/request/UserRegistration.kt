package jp.kyuuki.watching.spring.model.request

data class UserRegistration(
        var phoneNumber: PhoneNumber
)

data class PhoneNumber(
        var countryCode: Int,
        var original: String
)