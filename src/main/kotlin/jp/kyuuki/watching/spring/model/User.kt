package jp.kyuuki.watching.spring.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,
        var phoneNumber: String = "",
        var nickName: String = "",
        var apiKey: String = ""
)