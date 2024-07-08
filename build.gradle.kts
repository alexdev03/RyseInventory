plugins {
    java
    `java-library`
    id("io.github.goooler.shadow") version "8.1.8"
    `maven-publish`

}

group = "io.github.rysefoxx"
description = "RyseInventory"

allprojects {
    apply {
        plugin("java")
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }
    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }
    }
    version = "1.6.12"
}

subprojects {
    apply {
        plugin("java-library")
        plugin("io.github.goooler.shadow")
        plugin("maven-publish")
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveClassifier.set("all")
        mergeServiceFiles()
    }

    publishing {
        publications {
            create<MavenPublication>("shadow") {
                from(components["java"])
                artifact(tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar").get()) {
                    classifier = "all"
                }
            }
        }
        repositories {
            mavenLocal()
        }
    }

    tasks.named("publishShadowPublicationToMavenLocal") {
        dependsOn(tasks.named("shadowJar"))
    }
}

project(":plugin") {
    tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        dependsOn(project(":api").tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar"))
    }
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("all")
    mergeServiceFiles()
}
