plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kt.lint.gradle)
    `maven-publish`
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishAllLibraryVariants()
    }
    jvmToolchain(17)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "domain"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.livedata)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {}
        androidMain.dependencies {}
        iosMain.dependencies {}
    }
}

android {
    namespace = "com.sheltek.kwikstart.core"
    compileSdk = libs.versions.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(true)
}

group = extra["publishing.group"] as String
version = libs.versions.kwikstart.core.get()

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ShelTek/KwikStart-Core")
            credentials {
                username = System.getenv("REPO_READ_WRITE_USER")
                password = System.getenv("REPO_READ_WRITE_TOKEN")
            }
        }
    }
}
