package jp.kyuuki.watching.spring

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource

@SpringBootApplication
open class WatchingSpringApplication

fun main(args: Array<String>) {
    // https://reasonable-code.com/input-resources-file/
    val inputSterm = ClassPathResource("firebase-adminsdk.json").inputStream

    val options: FirebaseOptions = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(inputSterm))
            .build()

    // https://furiblog.com/firebase-admin-sdk-reload
    // なぜかここが 2 回通る
    val apps = FirebaseApp.getApps()
    if (apps.size == 0) {
        FirebaseApp.initializeApp(options)
    }

    runApplication<WatchingSpringApplication>(*args)
}
