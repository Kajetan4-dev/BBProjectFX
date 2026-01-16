plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = "22"
    modules("javafx.controls", "javafx.fxml", "javafx.media")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:22")
    implementation("org.openjfx:javafx-fxml:22")
    implementation("org.openjfx:javafx-media:22")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("at.ac.hcw.Game.Choice")
}