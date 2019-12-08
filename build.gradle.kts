plugins {
    application
    java
    kotlin("jvm") version "1.3.60"
}

group = "com.nott"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.flickr4java:flickr4java:3.0.2")
    implementation("com.github.ajalt:clikt:2.3.0")
    implementation("commons-io:commons-io:2.6")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

application {
    mainClassName = "com.nott.MainKt"
}