package jp.kyuuki.watching.spring.model.request

data class UpdateUser(
        val id: Int,
        var nickname: String
)