package jp.kyuuki.watching.spring.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "follow_requests")
data class FollowRequest(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,
        @ManyToOne
        val fromUser: User,
        @JsonIgnore
        @ManyToOne
        val toUser: User
)