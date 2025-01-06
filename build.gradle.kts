repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

rootProject.name = "SimpleVoiceChatBedrock"
include("app", "list", "utilities")
