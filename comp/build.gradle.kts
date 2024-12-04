//TODO using gradle 8.5 needed, but not specced here
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt") //TODO https://stackoverflow.com/questions/78316045/hilt-with-ksp-instead-of-kapt
    id("com.google.dagger.hilt.android") // https://developer.android.com/training/dependency-injection/hilt-android
    id("com.google.devtools.ksp")

    id("io.github.takahirom.roborazzi")
}

android {
    namespace = "com.example.comp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.comp"
        minSdk = 28 // Need this to run on my test phone running android 9 which is apparently api 28? (the phone might do 29)
        targetSdk = 34 // Preview was giving layout fidelity warnings saying it goes up to 33, libs complain about needing 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {//TODO any specific reason i started with java 1_8?, swiutched to 17 for no reason: https://hhtt.kr/103122
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    //TODO dunno what this does, adding for roborazzi
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}

dependencies {
    val nav_version = "2.8.4"
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("com.github.tony19:logback-android:3.0.0") // git readme specifies slf4j-api 2.0.7 but we'll see //TODO see other info there about test setup
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    //roborazzi stuff
    val roborazzi_version = "1.34.0"
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.14")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    testImplementation("androidx.navigation:navigation-testing:$nav_version")
    testImplementation("io.github.takahirom.roborazzi:roborazzi:$roborazzi_version")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:$roborazzi_version")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:$roborazzi_version")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

}

kapt {
    correctErrorTypes = true
}
