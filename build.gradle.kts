group = "dev.lynxplay"
version = "1.0.9-SNAPSHOT"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
tasks.shadowJar { archiveClassifier.set("final") }

plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}

tasks.withType(Javadoc::class) {
    options.encoding = Charsets.UTF_8.name()
}

tasks.withType<ProcessResources> { expand(project.properties); filteringCharset = Charsets.UTF_8.name() }
