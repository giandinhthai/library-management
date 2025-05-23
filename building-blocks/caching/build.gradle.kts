plugins {
    `java-library`
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

// Define versions in a central place (optional but recommended)
val jacksonVersion = "2.17.1" // Matches Spring Boot 3.4.4's Jackson
val redissonVersion = "3.46.0" // Matches Spring Boot 3.4.4's Redisson
dependencies {
    api("org.springframework.boot:spring-boot-starter-data-redis:3.4.4")
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("org.redisson:redisson-spring-boot-starter:$redissonVersion")


    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}