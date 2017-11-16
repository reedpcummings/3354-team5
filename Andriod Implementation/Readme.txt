Found solution for incomptaible Gradle version
https://stackoverflow.com/questions/24795079/error1-0-plugin-with-id-com-android-application-not-found

// Add this snipet to top of build.gradle file
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath "com.android.tools.build:gradle:3.0.0"
    }
}