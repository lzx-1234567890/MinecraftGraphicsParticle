import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    
}

android {
    namespace = "com.lzx.minecraftparticle"
    compileSdk = 33
    
    
    defaultConfig {
        applicationId = "com.lzx.minecraftparticle"
        minSdk = 21
        targetSdk = 33
        versionCode = 7
        versionName = "1.4.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties().apply {
        load(FileInputStream(keystorePropertiesFile))
    }
    
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        
    }
    
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    //implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    //implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.1")
    //implementation("io.github.afreakyelf:Pdf-Viewer:latest-version")
    implementation("com.dmitryborodin:pdfview-android:1.1.0")
    implementation("com.github.addisonelliott:SegmentedButton:3.1.9")
    //implementation("com.miguelcatalan:materialsearchview:1.4.0")
    //implementation ("com.github.MiguelCatalan:MaterialSearchView:v1.2.0")

}