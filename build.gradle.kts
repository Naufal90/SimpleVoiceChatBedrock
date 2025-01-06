plugins {
    kotlin("jvm") version "1.9.10" apply false
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

rootProject.name = "SimpleVoiceChatBedrock"
include("app", "list", "utilities")
