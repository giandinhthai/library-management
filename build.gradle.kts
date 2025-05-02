plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

subprojects {
    group = "com.example"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}