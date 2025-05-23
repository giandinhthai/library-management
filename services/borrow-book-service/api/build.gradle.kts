plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
    implementation(project(":services:borrow-book-service:application"))
    implementation(project(":services:borrow-book-service:domain"))
    implementation(project(":services:borrow-book-service:infra"))

    implementation(project(":building-blocks:cqrs"))
    implementation(project(":building-blocks:shared-kernel"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}