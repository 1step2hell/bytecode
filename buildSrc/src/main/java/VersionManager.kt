object Versions {
    val compileSdkVersion = 30
    val buildToolsVersion = "30.0.3"
    val minSdkVersion = 21
    val targetSdkVersion = 30

    val appcompat = "1.1.0"
    val material = "1.2.1"
    val recyclerView = "1.1.0"
    val fragment = "1.2.1"
    val constraintLayout = "2.0.1"
    val coreKtx = "1.3.0"
    val kotlin = "1.5.31"
    val coroutines = "1.3.7"
    val junit = "1.1.2"
    val ruler = "1.3.0"
    val runner = "1.3.0"
}

object Dependencies {
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val material = "com.google.android.material:material:${Versions.material}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    val constraintLayout ="androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val kotlinStdLibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val coroutines ="org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val junit = "androidx.test.ext:junit:${Versions.junit}"
    val ruler = "androidx.test:rules:${Versions.ruler}"
    val runner = "androidx.test:runner:${Versions.runner}"
}
