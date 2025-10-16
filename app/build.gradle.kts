plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)

    // Detekt plugin
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

android {
    namespace = "vcmsa.projects.wil_hustlehub"
    compileSdk = 35
    viewBinding.isEnabled = true

    defaultConfig {
        applicationId = "vcmsa.projects.wil_hustlehub"
        minSdk = 27
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.volley)
    implementation(libs.androidx.coordinatorlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.3.0"))

    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.3")
    implementation("com.google.android.material:material:1.12.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.android.material:material:1.11.0")

    //google login
    dependencies {

        implementation(libs.google.firebase.bom)
        implementation(libs.google.firebase.auth)
        implementation(libs.androidx.credentials.v130)
        implementation(libs.androidx.credentials.play.services.auth.v130)
        implementation(libs.googleid)
    }
}

// Detekt configuration
detekt {
    toolVersion = "1.22.0"
    config = files("config/detekt/detekt.yml") // create this file with your custom rules
    buildUponDefaultConfig = true
    allRules = false

    
    reports {
        html.required.set(true)   // Human-readable HTML
        xml.required.set(true)    // Can be used in CI/CD pipelines
        txt.required.set(false)   // Simple text report
        sarif.required.set(true)  // Useful for GitHub code scanning
    }
}

// Optional Detekt task for running analysis
tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAll") {
    description = "Runs detekt on the whole project."
    group = "verification"

    setSource(files("src/main/java", "src/main/kotlin"))
    include("**/*.kt")
    exclude("**/build/**")

    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}
