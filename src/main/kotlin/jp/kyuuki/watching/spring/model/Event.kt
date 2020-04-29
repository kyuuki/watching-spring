package jp.kyuuki.watching.spring.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,
        val description: String,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        val createdAt: Date,
        val user: User
)