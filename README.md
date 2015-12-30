# Android Social Login

[![Build Status](https://travis-ci.org/fanky10/android-social-login-module.svg?branch=master)](https://travis-ci.org/fanky10/android-social-login-module)

Android social login module for facebook, g+ whatever comes next

### Android Studio

Add to the top-level build file where you can add configuration options common to all sub-projects/modules.
```
buildscript {
    repositories {
        mavenCentral()
        maven { url 'https://maven.fabric.io/public' }
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'
        classpath 'io.fabric.tools:gradle:1.+'
        // Note: this will be changed to a more stable dependency
        classpath 'com.google.gms:google-services:1.5.0-beta2'
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven { url 'https://maven.fabric.io/public' }
        jcenter()
    }
}
```

Add to your application's `build.gradle` file

```
repositories {

    mavenCentral()

    maven {
        url "https://raw.githubusercontent.com/fanky10/android-social-login-module/master/mvn-repo"
    }
}

dependencies {
  compile 'io.github.fanky10:social-login-module:1.0.0'
}
```

### Maven Not supported Yet.

If you have any comments / suggestions feel free to open an issue.
If you have a fix, we expect a pull request (:
