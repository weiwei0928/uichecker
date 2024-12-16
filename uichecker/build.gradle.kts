plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.ww.tools.uichecker"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            buildConfigField ("boolean", "DEBUG_MODE", "true")
        }
        release {
            buildConfigField ("boolean", "DEBUG_MODE", "false")
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenRelease") {
            groupId = "com.ww.tools"
            artifactId = "uichecker"
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            url = uri("../repo")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}