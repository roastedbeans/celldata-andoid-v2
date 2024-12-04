// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

buildscript {
    val kotlinVersion by extra("1.9.22")

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle.v822)  // Changed from 8.7.3
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
