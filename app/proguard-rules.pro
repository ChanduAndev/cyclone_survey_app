# Custom ProGuard rules for this application

# Keep model classes if required by reflection or JSON parsing
# -keep class your.package.name.** { *; }

# Suppress missing class warnings for OkHttp optional dependencies
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.OpenSSLProvider