# Android Social Login

[![Build Status](https://travis-ci.org/fanky10/android-social-login-module.svg?branch=master)](https://travis-ci.org/fanky10/android-social-login-module)

Android social login module for facebook, twitter, g+ whatever comes next


## Android Studio


### Gradle

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
  compile 'io.github.fanky10:social-login-module:1.0.1'
}
```


### Maven 

Not supported Yet.


## Making use of the library

The library is divided in different fragments to implement: BaseLoginFragment, BaseFacebookFragment, etc.

So in the end the only thing left is:

- Implement the desired fragments.
- Create an Activity
- Add the implemented fragments and use callbacks to fire new activities accordingly. 


## Thanks to:

* @marianosalvetti

* @cpienovi

If you have any comments / suggestions feel free to open an issue.
If you have a fix, we expect a pull request (:
