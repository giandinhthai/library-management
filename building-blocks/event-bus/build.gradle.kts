plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":building-blocks:shared-kernel"))
    implementation("org.springframework.kafka:spring-kafka:3.3.5")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.boot:spring-boot-starter-json:3.4.4")

}