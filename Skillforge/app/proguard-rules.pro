# Keep Gson model classes (parsed reflectively from the API).
-keep class com.clickretina.skillforge.data.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
