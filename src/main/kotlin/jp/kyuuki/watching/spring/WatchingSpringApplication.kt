package jp.kyuuki.watching.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class WatchingSpringApplication

fun main(args: Array<String>) {
    runApplication<WatchingSpringApplication>(*args)
}
