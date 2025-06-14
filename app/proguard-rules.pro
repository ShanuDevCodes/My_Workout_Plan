# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Navigation Component classes
-keep class androidx.navigation.** { *; }
-keep class * extends androidx.navigation.NavDestination
-keep class * extends androidx.navigation.Navigator

# Keep your navigation destinations (sealed classes)
-keep class com.example.myworkoutplan.features.mainapp.ui.Destination { *; }
-keep class com.example.myworkoutplan.features.mainapp.ui.Destination$* { *; }
-keep class com.example.myworkoutplan.features.mainapp.ui.PlanDestination { *; }
-keep class com.example.myworkoutplan.features.mainapp.ui.PlanDestination$* { *; }

# Keep Compose Navigation classes
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.navigation.compose.** { *; }

# Keep your main activity and UI components
-keep class com.example.myworkoutplan.MainActivity { *; }
-keep class com.example.myworkoutplan.features.mainapp.ui.AdaptiveUI** { *; }

# Keep serializable classes used in navigation
-keepclassmembers class * implements kotlinx.serialization.KSerializer {
    *;
}
-keep,includedescriptorclasses class com.example.myworkoutplan.**$$serializer { *; }
-keepclassmembers class com.example.myworkoutplan.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.myworkoutplan.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep ViewModels and their factories
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.ViewModelProvider$Factory { *; }