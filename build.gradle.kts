plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven {
    url = uri("https://repo.papermc.io/repository/maven-public/")
}
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}
