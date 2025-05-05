plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencies for shared-kernel
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

}