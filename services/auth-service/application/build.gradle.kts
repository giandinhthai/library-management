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
    implementation(project(":building-blocks:shared-kernel"))
    implementation(project(":building-blocks:event-bus"))
    implementation("org.springframework:spring-context:6.2.5")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.4")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
}