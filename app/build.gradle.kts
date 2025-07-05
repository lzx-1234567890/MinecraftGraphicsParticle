import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    
}

android {
    namespace = "com.lzx.minecraftparticle"
    compileSdk = 36
    
    
    defaultConfig {
        applicationId = "com.lzx.minecraftparticle"
        minSdk = 26
        targetSdk = 33
        versionCode = 8
        versionName = "1.5.0"
        
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
    implementation("com.dmitryborodin:pdfview-android:1.1.0")
    implementation("com.github.addisonelliott:SegmentedButton:3.1.9")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.1")
    implementation("com.caverock:androidsvg:1.4")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("org.apache.tika:tika-core:2.4.1")
    implementation("org.apache.tika:tika-parsers:2.4.1")
    implementation("javax.xml.stream:stax-api:1.0-2")
    implementation("org.slf4j:slf4j-android:1.7.36")


}