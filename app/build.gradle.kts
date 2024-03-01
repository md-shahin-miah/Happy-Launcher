plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.happylauncher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.happylauncher"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {

        }
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }



}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
//    implementation("androidx.preference:preference:1.2.1"){
//        exclude group: 'androidx.lifecycle', module:'lifecycle-viewmodel'
//        exclude group: 'androidx.lifecycle', module:'lifecycle-viewmodel-ktx'
//    }

    implementation("androidx.preference:preference:1.2.1") {
        // Workaround for bug https://issuetracker.google.com/issues/238425626
//        exclude group: 'androidx.lifecycle', module:'lifecycle-viewmodel'
//        exclude group: 'androidx.lifecycle', module:'lifecycle-viewmodel-ktx'
    }
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}