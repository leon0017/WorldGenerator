import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("jvm") version "2.0.0-Beta2"
}

group = "me.leonrobi.worldgenerator"
version = "1"
description = "WorldGenerator"
var mainClass = "WorldGenerator"
var userHome: String = System.getProperty("user.home")
var mcPluginsDir = "$userHome\\Desktop\\WorldGeneratorTest\\plugins"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.md-5.net/content/groups/public/")
    }
    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-include-runtime"
        jvmTarget = "21"
    }
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21",
            "mainClass" to "${project.group}.${mainClass}"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        manifest {
            attributes("Main-Class" to ("$group.$mainClass"))
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(*(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }.toTypedArray()))

        destinationDirectory.set(file(mcPluginsDir))
        archiveFileName.set("${mainClass}.jar")
    }

    /*reobfJar {
        outputJar.set(file("$mcPluginsDir\\${mainClass}.jar"))
    }*/
}