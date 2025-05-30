plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.kt.worktimetrackermanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kt.worktimetrackermanager"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://localhost:5260/api/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "BASE_URL", "\"http://localhost:5260/api/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    //Gson converter
    implementation(libs.converter.gson)

    //Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    //Navigation
    implementation(libs.navigation)

    //Datastore
    implementation(libs.androidx.datastore.preferences)

    //Timber
    implementation(libs.timber)

    //Retrofit
    implementation(libs.retrofit)
    // OkHttp logging interceptor for debugging HTTP traffic
    implementation(libs.okHttp3.logging)

    //Splash screen
    implementation(libs.androidx.core.splashscreen)

    //ApiResponse
    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)

    //Material Theme
    implementation(libs.material)

    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Result
    implementation(libs.kotlin.result)

    //Kotlin Collection Immutable
    implementation(libs.kotlinx.collections.immutable)

    //Compose chart
    implementation("io.github.ehsannarmani:compose-charts:0.1.2")

    //Calendar
    implementation(libs.kizitonwose.calendar)

    //Vico Chart
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    //Jwt
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    //Snapper
    implementation("dev.chrisbanes.snapper:snapper:0.3.0")

    // Map
    implementation (libs.osmdroid.android)
    implementation(libs.play.services.location)

    // lịch
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.3.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:clock:1.3.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:option:1.3.0")

    //icon
    implementation ("androidx.compose.material:material-icons-extended:1.7.8")

    //Constraint layout
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation(kotlin("reflect"))
}