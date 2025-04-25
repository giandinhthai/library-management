plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation(project(":building-blocks:security"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
}