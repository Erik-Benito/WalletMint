// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    // Google
    id("com.google.gms.google-services") version "4.4.1" apply false

    // KSP
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false

    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.10")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath ("com.android.tools.build:gradle:7.0.4")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    }
}

