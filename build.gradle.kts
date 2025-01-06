plugins {
    id("buildlogic.kotlin-common-conventions")  // Ini sudah mencakup plugin Kotlin
    `java-library`
}

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.maxhenkel.de") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
