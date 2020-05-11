package jp.kyuuki.watching.spring.model

import javax.persistence.*

@Entity
data class Follow(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @ManyToOne
        val fromUser: User,

        @ManyToOne
        val toUser: User
)