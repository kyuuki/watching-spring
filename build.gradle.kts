import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.3.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"

	// https://kotlinlang.org/docs/reference/compiler-plugins.html#jpa-support
	id("org.jetbrains.kotlin.plugin.jpa") version "1.3.72"
}

group = "jp.kyuuki.watching"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	runtimeOnly("com.h2database:h2")
	// TODO: ローカルで実行するときは毎回コメントアウトする必要がある
	runtimeOnly("org.postgresql:postgresql")

	// https://github.com/google/libphonenumber/wiki/Android-Studio-setup
	implementation("com.googlecode.libphonenumber:libphonenumber:8.2.0")

	// https://firebase.google.com/docs/admin/setup
	implementation("com.google.firebase:firebase-admin:6.13.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
