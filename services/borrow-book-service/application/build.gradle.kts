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
    implementation(project(":building-blocks:cqrs"))
    implementation(project(":building-blocks:shared-kernel"))

    implementation(project(":services:borrow-book-service:domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")



    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}