// settings.gradle.kts

pluginManagement {
    repositories {
        gradlePluginPortal()  // Menambahkan Gradle Plugin Portal
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    plugins {
        id("com.android.application") version "8.1.0"
    }
}

rootProject.name = "SimpleVoiceChatBedrock"
include(":app")
