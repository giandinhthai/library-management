import java.io.File

fun createService(serviceName: String, basePackage: String = "com.example") {
    val servicePackage = "${basePackage}.${serviceName.toLowerCase().replace("-", "")}".replace("-service", "service")
    val servicePackagePath = servicePackage.replace(".", "/")
    val root = File("services/${serviceName}")

    println("Creating service: $serviceName with package: $servicePackage")

    // Create service directory
    root.mkdirs()

    fun createModule(name: String, withSpringBoot: Boolean = false) {
        val path = File(root, name)
        path.mkdirs()

        // src folder
        File(path, "src/main/java/$servicePackagePath").mkdirs()

        // build.gradle.kts
        File(path, "build.gradle.kts").writeText("""
plugins {
    java
    ${if (withSpringBoot) "id(\"org.springframework.boot\")\nid(\"io.spring.dependency-management\")" else ""}
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO: Add your dependencies
}
        """.trimIndent())
    }

    listOf(
        "api" to true,
        "application" to false,
        "domain" to false,
        "infra" to false,
    ).forEach { (name, withSpring) -> createModule(name, withSpring) }

    // Create main class for `app`
    val mainJava = File(root, "api/src/main/java/$servicePackagePath")
    File(mainJava, "${serviceName.capitalize().replace("-", "")}Application.java").writeText("""
package $servicePackage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ${serviceName.capitalize().replace("-", "")}Application {
    public static void main(String[] args) {
        SpringApplication.run(${serviceName.capitalize().replace("-", "")}Application.class, args);
    }
}
    """.trimIndent())

    // Update settings.gradle.kts
    updateSettingsGradle(serviceName)

    println("Service $serviceName created successfully!")
}

fun createBuildingBlock(name: String) {
    val buildingBlocksRoot = File("building-blocks")
    val blockPath = File(buildingBlocksRoot, name)
    blockPath.mkdirs()

    File(blockPath, "src/main/java/com/example/buildingblocks/$name").mkdirs()

    File(blockPath, "build.gradle.kts").writeText("""
plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencies for $name
}
    """.trimIndent())

    // Update settings.gradle.kts
    updateSettingsGradleForBuildingBlock(name)

    println("Building block $name created successfully!")
}

fun updateSettingsGradle(serviceName: String) {
    val settingsFile = File("settings.gradle.kts")
    if (!settingsFile.exists()) {
        // Create settings.gradle.kts if it doesn't exist
        settingsFile.writeText("""
        rootProject.name = "droppii-new-project"
        
        // Building Blocks
        
        // Services
        """.trimIndent())
    }

    // Read existing content
    val content = settingsFile.readText()

    // Check if service is already included
    val modules = listOf("api", "application", "domain", "infra", "app")
    val moduleIncludes = modules.map { "include(\":services:$serviceName:$it\")" }

    if (!moduleIncludes.any { content.contains(it) }) {
        // Append new service modules
        val newContent = content + "\n\n// $serviceName modules\n" +
                moduleIncludes.joinToString("\n")

        settingsFile.writeText(newContent)
        println("Updated settings.gradle.kts with $serviceName modules")
    } else {
        println("$serviceName modules already exist in settings.gradle.kts")
    }
}

fun updateSettingsGradleForBuildingBlock(blockName: String) {
    val settingsFile = File("settings.gradle.kts")
    if (!settingsFile.exists()) {
        // Create settings.gradle.kts if it doesn't exist
        settingsFile.writeText("""
        rootProject.name = "droppii-new-project"
        
        // Building Blocks
        
        // Services
        """.trimIndent())
    }

    // Read existing content
    val content = settingsFile.readText()

    // Check if building block is already included
    val includeStatement = "include(\":building-blocks:$blockName\")"

    if (!content.contains(includeStatement)) {
        // Find the building blocks section
        val buildingBlocksIndex = content.indexOf("// Building Blocks")
        if (buildingBlocksIndex >= 0) {
            // Insert after the building blocks section
            val beforeSection = content.substring(0, buildingBlocksIndex + "// Building Blocks".length)
            val afterSection = content.substring(buildingBlocksIndex + "// Building Blocks".length)
            val newContent = beforeSection + "\n$includeStatement" + afterSection

            settingsFile.writeText(newContent)
        } else {
            // Append to the end
            val newContent = content + "\n\n// Building Blocks\n$includeStatement"
            settingsFile.writeText(newContent)
        }
        println("Updated settings.gradle.kts with building block: $blockName")
    } else {
        println("Building block $blockName already exists in settings.gradle.kts")
    }
}

// Example usage:
// createService("auth-service")
// createService("circulation-service")

// Create building blocks
val blocks = listOf(
    "caching", "cqrs", "event-bus", "jpa", "resilience", "shared-kernel", "security"
)

// Uncomment to create building blocks
// blocks.forEach { createBuildingBlock(it) }
createService("loan-management-service")