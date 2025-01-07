plugins {
    id("com.android.application") version "8.1.0"
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

android {
    compileSdkVersion(33) 
}
