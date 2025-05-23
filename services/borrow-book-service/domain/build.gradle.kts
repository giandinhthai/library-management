plugins {
    java
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
    api(project(":building-blocks:shared-kernel"))

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")


}