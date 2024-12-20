// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.2.1" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("io.github.takahirom.roborazzi") version "1.34.0" apply false
}

//TODO not sure this actually does the expected
tasks.named<Wrapper>("wrapper") {
    gradleVersion = "8.5"
}