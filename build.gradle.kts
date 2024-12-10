// Top-level build file where you can add configuration options common to all sub-projects/modules.





plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
   id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
  // id ("org.jetbrains.kotlin.android") version "2.1.0" apply false
}




buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.3") // Update to the latest version
    }


}
