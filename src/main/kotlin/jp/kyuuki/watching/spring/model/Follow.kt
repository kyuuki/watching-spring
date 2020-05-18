package jp.kyuuki.watching.spring.model

import javax.persistence.*

@Entity
@Table(name = "follows")
data class Follow(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @ManyToOne
        val fromUser: User,

        @ManyToOne
        val toUser: User
)