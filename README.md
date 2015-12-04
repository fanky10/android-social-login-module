# android-social-login-module
Android social login module for facebook, g+ whatever comes next

# Add the corresponding maven repository configuration section to your gradle
repositories {
    mavenCentral()

    maven {
        url "https://raw.githubusercontent.com/fanky10/android-social-login-module/master/mvn-repo"
    }
}

compile 'io.github.fanky10:social-login-module:1.0.0'
