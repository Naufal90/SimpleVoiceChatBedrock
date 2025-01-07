// app/build.gradle.kts

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.naufal90.simplevoicechat"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        namespace = "com.naufal90.simplevoicechat"  // Tambahkan baris ini
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

repositories {
    mavenCentral()
    google()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1") // Dependensi tambahan yang umum digunakan
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("de.maxhenkel.voicechat:voicechat-api:2.1.12") // Dependensi untuk VoiceChat API
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT") // Dependensi untuk PaperMC
    implementation(kotlin("stdlib"))
}
