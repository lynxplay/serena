group = "dev.lynxplay"
version = "1.0.0-SNAPSHOT"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
tasks.shadowJar { archiveClassifier.set("final") }

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.1-R0.1-SNAPSHOT")
}

tasks.withType(Javadoc::class) {
    options.encoding = Charsets.UTF_8.name()
}

tasks.withType<ProcessResources> { expand(project.properties); filteringCharset = Charsets.UTF_8.name() }
