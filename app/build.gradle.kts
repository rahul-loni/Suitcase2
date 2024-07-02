plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.suitcase2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.suitcase2"
        minSdk = 24
        targetSdk = 34
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.cast.framework)
    testImplementation(libs.junit)

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}