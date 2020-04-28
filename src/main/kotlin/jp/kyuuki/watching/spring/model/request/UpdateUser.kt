package jp.kyuuki.watching.spring.model.request

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class UpdateUser(
        val id: Int,
        var nickname: String
)