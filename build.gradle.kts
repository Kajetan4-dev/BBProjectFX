plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

javafx {
    version = "23"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX Bibliotheken
    implementation("org.openjfx:javafx-controls:23")
    implementation("org.openjfx:javafx-fxml:23")
    implementation("org.openjfx:javafx-media:23")

    // Test-Bibliotheken
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("at.ac.hcw.Game.Choice")
}
