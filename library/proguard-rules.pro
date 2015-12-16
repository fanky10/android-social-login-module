# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\facundo.scoccia\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

##---------------Begin: proguard configuration for Android Social Login Module ----------
-target 1.6
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
#-dontobfuscate
#-dontoptimize
-dontnote
-repackageclasses ''
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-ignorewarnings
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends com.google.inject.AbstractModule
-keep public class com.android.vending.licensing.ILicensingService
-keep class com.google.inject.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keepclasseswithmembers class * { native <methods>; }

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init> (android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init> (android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * implements android.os.Parcelable { static android.os.Parcelable$Creator *; }
-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}
-keepclassmembers class **.R$* { public static <fields>; }
-keepclasseswithmembernames class * { native <methods>; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * extends android.app.Activity {
	public void *(android.view.View);
}

-keepattributes **
-keepattributes EnclosingMethod
-keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod


### - Support Library v4 specific rules --
-dontwarn android.support.v4.**

#### -- Support Library v7  specific rules  --
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }


### Facebook SDK Setting
-keep class com.facebook.** { *; }
-keepattributes Signature

#### Twitter SDK Setting
-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

##---------------End: proguard configuration for Android Social Login Module  ----------
