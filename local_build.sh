#!/bin/bash
# Local APK Build Script
# 
# Prerequisites:
# 1. Install Android Studio or command-line SDK tools
# 2. Set ANDROID_HOME environment variable
# 3. Install JDK 17+
#
# On macOS:
#   brew install --cask android-studio
#   export ANDROID_HOME=$HOME/Library/Android/sdk
#   $ANDROID_HOME/tools/bin/sdkmanager "platforms;android-34" "build-tools;34.0.0"
#
# Then run this script:
#   chmod +x local_build.sh
#   ./local_build.sh

set -e

# Check requirements
if [ -z "$ANDROID_HOME" ]; then
    echo "Error: ANDROID_HOME is not set"
    echo "Please set it to your Android SDK path"
    exit 1
fi

# Build
echo "Building APK..."
cd "$(dirname "$0")"

# Download gradle wrapper if needed
if [ ! -f "gradlew" ]; then
    echo "Downloading gradle wrapper..."
    gradle wrapper --gradle-version 8.5
fi

chmod +x gradlew
./gradlew assembleDebug

echo ""
echo "✅ APK built successfully!"
echo "Location: $(pwd)/app/build/outputs/apk/debug/app-debug.apk"
