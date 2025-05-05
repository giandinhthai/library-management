plugins {
    java
    
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("org.springframework:spring-context:6.2.5")
    implementation("org.springframework.retry:spring-retry:2.0.11")
    implementation(project(":building-blocks:cqrs"))
    implementation(project(":building-blocks:shared-kernel"))
    implementation(project(":building-blocks:event-bus"))

    implementation(project(":services:borrow-book-service:domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.4")

    implementation("org.springframework.kafka:spring-kafka:3.3.5")



    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")



}