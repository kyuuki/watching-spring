package jp.kyuuki.watching.spring.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,
        @JsonIgnore
        var phoneNumber: String = "",
        @JsonProperty("nick_name")
        var nickname: String = "",
        @JsonIgnore
        var apiKey: String = ""
)