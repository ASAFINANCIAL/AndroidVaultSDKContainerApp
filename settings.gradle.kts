pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
// import mavenLocal to be able to resolve the "nativeLibrary"
        mavenLocal()
        // ASAVault SDK private Maven registry (GitHub Packages).
        // Credentials: set gpr.user / gpr.token in ~/.gradle/gradle.properties
        // (or GH_PACKAGES_USER / GH_PACKAGES_TOKEN env vars). Token needs read:packages.
        maven {
            name = "ASAVaultGitHubPackages"
            url = uri("https://maven.pkg.github.com/ASAFINANCIAL/android-asavaultsdk-maven")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                    ?: System.getenv("GH_PACKAGES_USER") ?: ""
                password = providers.gradleProperty("gpr.token").orNull
                    ?: System.getenv("GH_PACKAGES_TOKEN") ?: ""
            }
        }
    }
}

rootProject.name = "ASAVaultSDKContainer"
include(":app")
 