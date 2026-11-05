# 1. Ignorar clases faltantes de Rhino/JavaScript que no existen en Android
-dontwarn java.beans.**
-dontwarn javax.script.**
-dontwarn org.mozilla.javascript.**
-keep class org.mozilla.javascript.** { *; }

# 2. Reglas para NewPipe Extractor
-keep class org.schabi.newpipe.extractor.** { *; }
-dontwarn org.schabi.newpipe.extractor.**

# 3. Reglas para yt-dlp (JunkFood02 / yausername)
-keep class com.yausername.youtubedl_android.** { *; }
-dontwarn com.yausername.youtubedl_android.**

# 4. Mantener métodos nativos C/C++ y anotaciones
-keepattributes *Annotation*
-keepclassmembers class * {
    native <methods>;
}