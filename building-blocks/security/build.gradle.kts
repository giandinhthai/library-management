plugins {
    java
    id("java-library")
//    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example.buildingblocks"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Dependencies for caching
    api("org.springframework.boot:spring-boot-autoconfigure:3.4.4")
    implementation("org.springframework:spring-context:6.2.5")
    api("org.springframework.boot:spring-boot-starter-security:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")


    // Lombok for reducing boilerplate
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

}