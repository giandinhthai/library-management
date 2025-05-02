plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
    implementation(project(":services:auth-service:domain"))
    implementation(project(":services:auth-service:application"))
    implementation(project(":services:auth-service:infra"))
    implementation(project(":building-blocks:cqrs"))
    implementation(project(":building-blocks:shared-kernel"))
    implementation(project(":building-blocks:event-bus"))
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.4")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

springBoot {
    mainClass.set("com.example.authservice.AppApplication")
}
