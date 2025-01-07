// settings.gradle.kts

pluginManagement {
    repositories {
        gradlePluginPortal()  // Menambahkan Gradle Plugin Portal
        mavenCentral()
        google()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    plugins {
        id("com.android.application") version "8.1.0" apply false  // Menambahkan `apply false` untuk memastikan hanya diterapkan di subproyek yang relevan
        id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "SimpleVoiceChatBedrock"
include(":app")
