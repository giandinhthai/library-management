plugins {
    java
    
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
    implementation(project(":services:borrow-book-service:domain"))
    implementation(project(":services:borrow-book-service:application"))
    implementation("org.postgresql:postgresql:42.7.5")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")


    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")


}