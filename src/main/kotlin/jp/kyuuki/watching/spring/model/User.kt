package jp.kyuuki.watching.spring.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "users")  // PostgreSQL で user テーブルは作成できない
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @JsonIgnore
        @Column(nullable = false)
        var phoneNumber: String,

        var nickname: String? = null,

        @JsonIgnore
        @Column(nullable = false, unique = true)
        var apiKey: String
)