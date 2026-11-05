# Evita que el optimizador de Release borre funciones de NewPipe y yt-dlp
-keep class com.yausername.youtubedl_android.** { *; }
-dontwarn com.yausername.youtubedl_android.**

-keep class org.schabi.newpipe.extractor.** { *; }
-dontwarn org.schabi.newpipe.extractor.**

-keepattributes *Annotation*
-keepclassmembers class * {
    native <methods>;
}