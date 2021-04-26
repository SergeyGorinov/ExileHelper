-keepattributes *Annotation*, InnerClasses

-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.sgorinov.exilehelper.**$$serializer { *; }
-keepclassmembers class com.sgorinov.exilehelper.** {
    *** Companion;
}
-keepclasseswithmembers class com.sgorinov.exilehelper.** {
    kotlinx.serialization.KSerializer serializer(...);
}