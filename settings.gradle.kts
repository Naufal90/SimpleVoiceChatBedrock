// settings.gradle.kts

pluginManagement {
    repositories {
        gradlePluginPortal()  // Menambahkan Gradle Plugin Portal
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    plugins {
        id("com.android.application") version "7.4.2"
    }
}

rootProject.name = "SimpleVoiceChatBedrock"
include(":app")
