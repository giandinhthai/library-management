plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":services:auth-service:domain"))
    implementation(project(":building-blocks:cqrs"))
    implementation(project(":building-blocks:security"))
    implementation("org.springframework:spring-context:6.2.5")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    // TODO: Add your dependencies
}