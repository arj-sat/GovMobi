plugins {
    alias(libs.plugins.android.application)
//    id("com.android.application")
    id("com.google.gms.google-services")
    id("jacoco") // Plugin placed in the top plugins block
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableAndroidTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }
}

jacoco {
    toolVersion = "0.8.10"
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.espresso.intents)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    androidTestImplementation("org.mockito:mockito-core:5.2.0")
    androidTestImplementation("org.mockito:mockito-android:5.2.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.2.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.espresso.intents)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test:rules:1.5.0")

    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // UiAutomator - Required for automatically clicking permission dialogs
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val buildDirPath = layout.buildDirectory.get().asFile

    val classDirs = fileTree(buildDirPath) {
        include(
            "intermediates/javac/debug/**/classes/**/*.class",
            "tmp/kotlin-classes/debug/**/*.class",
            "intermediates/compile_app_classes_jar/debug/**/*.class"
        )
        exclude(fileFilter)
    }

    val mainSrc = "${project.projectDir}/src/main"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(classDirs))

    val unitTestCoverage = fileTree(layout.buildDirectory.get().asFile) {
        include("jacoco/testDebugUnitTest.exec")
    }

    val instrumentedCoverage = fileTree(
        buildDirPath.resolve("outputs/code_coverage/debugAndroidTest/connected")
    ) {
        include("**/coverage.ec")
    }

    executionData.setFrom(files(unitTestCoverage, instrumentedCoverage))

    doFirst {
        if (unitTestCoverage.isEmpty && instrumentedCoverage.isEmpty) {
            throw GradleException("No coverage data found. Run tests before generating report.")
        }
    }
}
