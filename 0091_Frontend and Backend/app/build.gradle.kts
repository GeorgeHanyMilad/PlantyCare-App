plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.android)
}


android {
    namespace = "graduation.plantcare"
    compileSdk = 35

    defaultConfig {
        applicationId = "graduation.plantcare"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.2"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.appcompat)
    implementation(libs.material.v190)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.material.v1110)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.core)
    implementation(libs.core.ktx)
    implementation(libs.gifdecoder)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.glide)
    implementation(libs.gson)
    annotationProcessor(libs.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.konfetti.xml)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.android.mail)
    implementation(libs.android.activation)
    implementation(libs.firebase.firestore)
    implementation(libs.google.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.dynamic.links.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.circleimageview)
}