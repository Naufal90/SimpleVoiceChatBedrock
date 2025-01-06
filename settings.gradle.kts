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
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "SimpleVoiceChatBedrock"
include(":app")
