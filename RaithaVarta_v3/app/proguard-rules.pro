# Default ProGuard rules for Raitha-Varta release builds.
# Most of the standard AndroidX / Material rules are already
# bundled in proguard-android-optimize.txt referenced from
# build.gradle.kts. Add app-specific keep rules below.

# Keep our data classes (used reflectively by ViewBinding/Adapters)
-keep class com.raithavarta.app.data.** { *; }

# Keep Application class
-keep class com.raithavarta.app.RaithaVartaApp { *; }

# AndroidX / Material defaults
-dontwarn com.google.android.material.**
-dontwarn androidx.**
-keep class com.google.android.material.** { *; }
-keep class androidx.appcompat.** { *; }

# Kotlin metadata
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations
-keepattributes Signature,InnerClasses,EnclosingMethod
