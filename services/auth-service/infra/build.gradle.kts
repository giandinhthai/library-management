plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}
group = "com.example.authservice"
version = "1.0.0"
repositories {
    mavenCentral()

}

dependencies {
    // TODO: Add your dependencies
    implementation(project(":services:auth-service:domain"))
    implementation(project(":services:auth-service:application"))
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
    implementation("org.postgresql:postgresql:42.7.5")

    implementation(project(":building-blocks:security"))

    // JWT dependencies
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")



}