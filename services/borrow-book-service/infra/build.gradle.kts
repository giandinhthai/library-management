plugins {
    java
    `java-library`
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
    implementation("org.springframework.retry:spring-retry:2.0.11")

    implementation(project(":services:borrow-book-service:domain"))
    implementation(project(":services:borrow-book-service:application"))
    implementation("org.postgresql:postgresql:42.7.5")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    implementation("org.springframework.boot:spring-boot-starter-security:3.4.4")

    testRuntimeOnly("com.h2database:h2")  // For testing with in-memory database
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.4")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
sourceSets {
    test {
        java {
            srcDirs("src/test/java")
        }
    }
}
tasks.test {
    useJUnitPlatform()
}