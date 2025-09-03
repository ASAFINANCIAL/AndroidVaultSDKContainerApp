# ASA Vault SDK Container - Android Integration Guide

This guide will help you integrate the ASA Vault SDK as an AAR file into your Android application. The SDK provides React Native functionality wrapped in a native Android AAR.

## Table of Contents
- [Prerequisites](#prerequisites)
- [AAR Integration](#aar-integration)
- [Dependencies](#dependencies)
- [Permissions](#permissions)
- [Firebase Setup](#firebase-setup)
- [Initialization](#initialization)
- [Activity Integration](#activity-integration)
- [Configuration](#configuration)
- [Usage Examples](#usage-examples)
- [Troubleshooting](#troubleshooting)

## Prerequisites

### System Requirements
- **Android Studio**: Latest stable version (recommended)
- **Minimum SDK Version**: 24 (Android 7.0)
- **Target SDK Version**: 35 (Android 14)
- **Java Version**: 11 or higher
- **Kotlin**: Latest stable version

### Development Environment
- Ensure you have a working Android development environment
- Make sure you can build and run Android applications successfully

## AAR Integration

### Step 1: Copy AAR to Maven Local

1. **Locate your Maven Local directory:**

   **macOS:**
   ```bash
   ~/.m2/repository/
   ```

   **Windows:**
   ```cmd
   C:\Users\{YourUsername}\.m2\repository\
   ```

2. **Copy the ASA Vault folder** provided by the ASA Vault developers to your Maven Local directory.

3. **Verify the structure** - it should look like:
   ```
   ~/.m2/repository/com/asavault/nativeLibrary/{version}/
   ```

### Step 2: Configure Maven Local Repository

Add `mavenLocal()` to your project's `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal() // Add this line
    }
}
```

## Dependencies

Add the following dependencies to your `app/build.gradle.kts` file:

```kotlin
dependencies {
    // ASA Vault SDK Library
    // Note: Use the same version name as the folder you copied to Maven Local
    implementation("com.asavault:nativeLibrary:{version}")
    
    // Lottie dependency for react-native-lottie support
    implementation("com.airbnb.android:lottie:6.3.0")
    
    // Biometric authentication
    implementation("androidx.biometric:biometric:1.1.0")
    
    // Coil dependency for image loading (required by AsaVault SDK)
    implementation("io.coil-kt.coil3:coil:3.0.4")
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    
    // Fresco dependencies for React Native image/gif support
    implementation("com.facebook.fresco:fresco:3.4.0")
    implementation("com.facebook.fresco:animated-gif:3.6.0")
    
    // Firebase dependencies for react-native-firebase support
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-dynamic-links")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-config-ktx")
}
```

**Important:** Replace `{version}` with the actual version number that matches the folder name you copied to Maven Local.

## Permissions

Add the following permissions to your `app/src/main/AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.USE_BIOMETRIC" />

<!-- Location permissions - choose based on your accuracy requirements -->
<!-- Define ACCESS_FINE_LOCATION if you will use enableHighAccuracy=true -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

<!-- Define ACCESS_COARSE_LOCATION if you will use enableHighAccuracy=false -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

<application
    android:name=".YourApplicationClass"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/Theme.YourApp">
    
    <!-- Your activities here -->
    
    <!-- Email client queries for ASA Vault SDK -->
    <queries>
        <!-- For Gmail -->
        <intent>
            <action android:name="android.intent.action.VIEW" />
            <data android:scheme="mailto" />
        </intent>
        <!-- For Outlook -->
        <intent>
            <action android:name="android.intent.action.VIEW" />
            <data android:scheme="ms-outlook" />
        </intent>
        <!-- For default email clients -->
        <intent>
            <action android:name="android.intent.action.VIEW" />
            <data android:scheme="mailto" />
        </intent>
    </queries>
    
</application>
```

### Permission Usage Notes:
- **Network permissions**: Required for API communication
- **Biometric permission**: Required for fingerprint/face authentication features
- **Location permissions**: Choose between FINE and COARSE based on your accuracy requirements

## Firebase Setup

### Step 1: Create Firebase Project

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. **Important**: Create an Android app with the **same package name** as your application

### Step 2: Download and Add google-services.json

1. Download the `google-services.json` file from Firebase Console
2. Move the downloaded `google-services.json` file into your module (app-level) root directory:
   ```
   your-project/
   ├── app/
   │   ├── google-services.json  ← Place here
   │   ├── build.gradle.kts
   │   └── src/
   └── build.gradle.kts
   ```

### Step 3: Configure Google Services Plugin

#### For Kotlin DSL (build.gradle.kts)

**Root-level (project-level) Gradle file** (`<project>/build.gradle.kts`):
```kotlin
plugins {
    // ... other plugins

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.3" apply false
}
```

**Module (app-level) Gradle file** (`<project>/<app-module>/build.gradle.kts`):
```kotlin
plugins {
    id("com.android.application")
    
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    
    // ... other plugins
}

dependencies {
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))
    
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}
```

#### For Groovy DSL (build.gradle)

**Root-level (project-level) Gradle file** (`<project>/build.gradle`):
```groovy
plugins {
    // ... other plugins

    // Add the dependency for the Google services Gradle plugin
    id 'com.google.gms.google-services' version '4.4.3' apply false
}
```

**Module (app-level) Gradle file** (`<project>/<app-module>/build.gradle`):
```groovy
plugins {
    id 'com.android.application'
    
    // Add the Google services Gradle plugin
    id 'com.google.gms.google-services'
    
    // ... other plugins
}

dependencies {
    // Import the Firebase BoM
    implementation platform('com.google.firebase:firebase-bom:34.2.0')
    
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation 'com.google.firebase:firebase-analytics'
    
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}
```

### Step 4: Verify Firebase Configuration

After adding the `google-services.json` file and configuring the plugin:
1. Sync your project with Gradle files
2. Build your project to ensure Firebase is properly integrated
3. Check that Firebase Analytics is working by viewing the Firebase Console

## Initialization

### Step 1: Create or Update Application Class

Create a new Application class or update your existing one to initialize the ASA Vault SDK. Your Application class should implement `ReactApplication` and include the necessary initialization code.

**File**: `app/src/main/java/com/yourpackage/YourApplicationClass.kt`

```kotlin
package com.yourpackage

import android.app.Application
import com.asavault.nativelibary.ReactNativeHostManagerNew
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.google.firebase.FirebaseApp

class YourApplicationClass : Application(), ReactApplication {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize React Native Brownfield
        ReactNativeHostManagerNew.initialize(this) {
            println("✅ React Native JS bundle loaded successfully")
        }
    }
    
    override val reactNativeHost: ReactNativeHost
        get() = com.callstack.reactnativebrownfield.ReactNativeBrownfield.shared.reactNativeHost
            ?: throw IllegalStateException("ReactNativeHost not initialized. Make sure ReactNativeHostManagerNew.initialize() was called.")

    override val reactHost: ReactHost
        get() = getDefaultReactHost(applicationContext, reactNativeHost)
}
```

### Step 2: Update AndroidManifest.xml

Make sure your Application class is declared in the `AndroidManifest.xml`:

```xml
<application
    android:name=".YourApplicationClass"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/Theme.YourApp">
    
    <!-- Your activities here -->
    
</application>
```

### Step 3: Verify Initialization

After implementing the Application class:
1. Build your project to ensure there are no compilation errors
2. Run the app and check the logcat for the success message: `"✅ React Native JS bundle loaded successfully"`
3. Ensure Firebase is properly initialized

## Activity Integration

### Step 1: Create AsaVaultActivity

Create a new Activity file in your project to integrate the ASA Vault SDK. This activity will handle the React Native view and permissions.

**File**: `app/src/main/java/com/yourpackage/AsaVaultActivity.kt`

```kotlin
package com.yourpackage

import android.os.Bundle
import android.util.Log
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

// Import that will be resolved once you install "nativelibary"
import com.asavault.nativelibary.VaultAppActivity
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener

class AsaVaultActivity : AppCompatActivity(), PermissionAwareActivity {
    private var permissionListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("RNapp", "onCreate: intent : ${intent.data} ")
        // ✅ Everything handled by the library!

        recreateVaultView()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // ✅ Let the library handle full screen mode
            // This is already handled in openAsaVault()
        }
    }

    override fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        listener: PermissionListener?   // <-- nullable to match RN's interface
    ) {
        permissionListener = listener
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    private fun recreateVaultView() {
        val initialParams = Bundle().apply {
            // SDK Configuration - Dynamic Link Setup as nested object
            val dynamicLinkBundle = Bundle().apply {
                putString("androidRootBundle", "com.asa.pal")
                putString("env", "QA")
            }
            putBundle("dynamicLinkSetup", dynamicLinkBundle)
            
            // SDK Configuration - AsaVault SDK Parameters as nested object
            // Note: These values will be provided by ASA Vault developers
            val sdkParamsBundle = Bundle().apply {
                putString("Subscriptionkey", "KEY_PLACEHOLDER")
                putInt("AsaFintechCode", AsaFintechCode_PLACEHOLDER)
                putInt("ApplicationCode", ApplicationCode_PLACEHOLDER)
                putString("AuthorizationKey", "AuthorizationKey_PLACEHOLDER")
            }
            putBundle("ASA Vault SDKparamter", sdkParamsBundle)
            
            val messageId = intent?.getStringExtra("messageId")
            Log.d("messageId", "onCreate: ")
            if (messageId != null) {
                putString("initialMessageId", messageId)
            }
            
            val initialUrl = intent?.getStringExtra("initialUrl")
            Log.d("messageId", "onCreate: ${initialUrl}")
            if (initialUrl != null) {
                putString("initialLink", initialUrl)
            }
        }
        
        val rnView = VaultAppActivity.openAsaVault(
            this,
            initialParams
        )
        
        VaultAppActivity.setOnCloseAsaVaultCallback {
            // Your custom logic here
            finish()
        }
        
        // Note: This callback is triggered when the user presses the "Close" or "Return" button
        // inside the ASA Vault package. You can override this behavior to implement custom
        // navigation logic, such as returning to a specific screen or performing cleanup tasks.

        setContentView(rnView)
        forwardInitialIntentToRN()
    }

    private fun forwardInitialIntentToRN() {
        val app = application as com.facebook.react.ReactApplication
        val rim = app.reactNativeHost.reactInstanceManager

        Log.d("forwardInitialIntentToRN", "forwardInitialIntentToRN: ${intent.data} ")
        // If React context is already created, forward immediately…
        if (rim.currentReactContext != null) {
            rim.onNewIntent(intent)
            return
        }

        // …otherwise, wait until it is ready, then forward once.
        rim.addReactInstanceEventListener(object :
            com.facebook.react.ReactInstanceManager.ReactInstanceEventListener {
            override fun onReactContextInitialized(context: com.facebook.react.bridge.ReactContext) {
                rim.onNewIntent(this@AsaVaultActivity.intent)
                rim.removeReactInstanceEventListener(this)
            }
        })
    }
    
    override fun onNewIntent(intent: Intent) {
        Log.d("RNApp", "onNewIntent: ${intent} , ${intent.data}")
        super.onNewIntent(intent)

        setIntent(intent)
        if (intent.getStringExtra("initialUrl") != null) {
            recreateVaultView()
        }
        forwardInitialIntentToRN()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Convert Array<out String> -> Array<String>
        val perms: Array<String> = permissions.toList().toTypedArray()

        // Give RN its callback first
        val consumed = permissionListener?.onRequestPermissionsResult(
            requestCode, perms, grantResults
        ) == true

        // Clear the reference (one-shot)
        permissionListener = null

        // Keep normal AppCompat flow if RN didn't consume it
        if (!consumed) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
```

### Step 2: Update AndroidManifest.xml

Add the AsaVaultActivity to your `AndroidManifest.xml`:

```xml
<application
    android:name=".YourApplicationClass"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/Theme.YourApp">
    
    <!-- Your main activity -->
    <activity
        android:name=".MainActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    
    <!-- ASA Vault Activity -->
    <activity
        android:name=".AsaVaultActivity"
        android:launchMode="singleTask"
        android:exported="true">
    </activity>
    
</application>
```

### Step 3: Important Notes

⚠️ **Configuration Values**: The following values in `initialParams` will be provided by ASA Vault developers:
- `Subscriptionkey`
- `AsaFintechCode`
- `ApplicationCode`
- `AuthorizationKey`

Do not hardcode these values. They should be replaced with the actual values provided by ASA Vault.

### Step 4: Launch the Activity

To launch the AsaVaultActivity from your main activity or any other part of your app:

```kotlin
val intent = Intent(this, AsaVaultActivity::class.java).apply {
    putExtra("messageId", "your_message_id")  // Optional - for push notification navigation
    putExtra("initialUrl", "your_initial_url") // Optional - for deep link navigation
}
startActivity(intent)
```

**Parameters Explained:**
- **`messageId`**: Used when the app is opened from a push notification. This parameter helps the ASA Vault SDK navigate to the specific content related to the notification.
- **`initialUrl`**: Used when the app is opened from a deep link. This parameter helps the ASA Vault SDK navigate to the specific URL or screen within the SDK.

## Configuration

### App Links / Deep Links Setup

To handle deep links that open the ASA Vault SDK, you need to add URL validation in your activity that handles deep links (typically your SplashActivity or MainActivity).

#### Step 1: Add URL Validation

In your activity that handles deep links, add the following validation check:

```kotlin
// In your SplashActivity or MainActivity
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Handle deep links
    handleDeepLink(intent)
}

private fun handleDeepLink(intent: Intent) {
    if (intent.data != null && VaultAppActivity.isASAVaultSDKUrl(intent.data.toString())) {
        // This is an ASA Vault SDK URL, redirect to AsaVaultActivity
        Intent(this@SplashActivity, AsaVaultActivity::class.java).setData(intent.data).apply {
            putExtras(Bundle().apply {
                putString("initialUrl", intent.data.toString())
            })
        }.also { startActivity(it) }
        finish() // Close the current activity
    } else {
        // Handle other deep links or continue with normal app flow
        // Your existing deep link handling logic
    }
}

override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleDeepLink(intent)
}
```

#### Step 2: Update AndroidManifest.xml for Deep Links

Add intent filters to your main activity or create a dedicated activity for handling deep links:

```xml
<activity
    android:name=".SplashActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
    
    <!-- Deep link handling -->
    <intent-filter android:autoVerify="true">
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        
        <!-- Add your app's deep link scheme -->
        <data android:scheme="https" />
        <data android:host="your-domain.com" />
        <!-- Add more data tags for specific paths if needed -->
    </intent-filter>
</activity>
```

#### Step 3: URL Validation Function

The `VaultAppActivity.isASAVaultSDKUrl()` function validates if the incoming URL is meant for the ASA Vault SDK. This ensures that only relevant deep links are handled by the AsaVaultActivity.

### Firebase Notification Setup

To handle ASA Vault notifications properly, you need to add notification validation in your Firebase Messaging Service.

#### Step 1: Notification Service Handler

In your Firebase Messaging Service, add the following function to handle ASA Vault notifications:

```kotlin
// In your FirebaseMessagingService class
private fun showNotification(remoteMessage: RemoteMessage) {
    val isASANotification = VaultAppActivity.isASAVaultNotification(remoteMessage)
    val isRNAppRunning = VaultAppActivity.isActivityRunning()
    
    if (isASANotification && !isRNAppRunning) {
        // Get title and body from notification or data payload
        val title = remoteMessage.notification?.title ?: ""
        val body = remoteMessage.notification?.body ?: ""

        // Create intent for when notification is tapped
        val intent = Intent(this, AsaVaultActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            
            // Add messageId from Firebase notification
            val messageId = remoteMessage.messageId
            if (messageId != null) {
                putExtra("messageId", messageId)
            }
            
            // Add notification data to intent
            for ((key, value) in remoteMessage.data) {
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

    } else {
        // Handle app host other notifications
        Log.d("Notification", "This is not an ASA Vault notification")
    }
}

override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    
    // Call the notification handler
    showNotification(remoteMessage)
}
```

#### Step 2: Splash Activity Notification Handling

In your SplashActivity, add the following code to handle notifications when the app is opened from a notification:

```kotlin
// In your SplashActivity onCreate method
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    val asavaultExtras = VaultAppActivity.isASAVaultExtras(intent.extras)

    // Determine which activity to launch based on extras data
    val targetIntent = if (asavaultExtras != null) {
        Log.d(TAG, "App opened from notification, launching AsaVaultActivity ${asavaultExtras}")
        Intent(this@SplashActivity, AsaVaultActivity::class.java).apply {
            // Pass notification data to AsaVaultActivity
            putExtras(asavaultExtras)
        }
    } else {
        // Launch your normal main activity
        Intent(this@SplashActivity, MainActivity::class.java)
    }
    
    startActivity(targetIntent)
    finish()
}
```

#### Step 3: Notification Functions Explained

- **`VaultAppActivity.isASAVaultNotification(remoteMessage)`**: Validates if the incoming notification is from ASA Vault
- **`VaultAppActivity.isActivityRunning()`**: Checks if the ASA Vault activity is currently running
- **`VaultAppActivity.isASAVaultExtras(extras)`**: Validates if the app was opened from an ASA Vault notification

### Additional Configuration Notes

- **Notification Channel**: Make sure you have created a notification channel for your app
- **Message ID**: The `messageId` from Firebase is passed to the AsaVaultActivity for proper handling
- **Data Payload**: All notification data is forwarded to the AsaVaultActivity for processing

## Usage Examples

### Basic Integration

The ASA Vault SDK is designed to be flexible and can be integrated into any part of your app. Here are some common usage patterns:

#### Example 1: Simple Button Click

```kotlin
// In your MainActivity or any other activity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Find your button
        val openAsaVaultButton = findViewById<Button>(R.id.btn_open_asavault)
        
        openAsaVaultButton.setOnClickListener {
            // Launch ASA Vault SDK
            val intent = Intent(this, AsaVaultActivity::class.java)
            startActivity(intent)
        }
    }
}
```

#### Example 2: Navigation from Menu Item

```kotlin
// In your activity with navigation drawer or bottom navigation
override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.nav_asavault -> {
            // Launch ASA Vault SDK
            val intent = Intent(this, AsaVaultActivity::class.java)
            startActivity(intent)
            return true
        }
        // Handle other menu items...
    }
    return false
}
```

#### Example 3: Deep Link Navigation

```kotlin
// When handling deep links in your activity
private fun handleDeepLink(url: String) {
    if (VaultAppActivity.isASAVaultSDKUrl(url)) {
        val intent = Intent(this, AsaVaultActivity::class.java).apply {
            putExtra("initialUrl", url)
        }
        startActivity(intent)
    }
}
```

#### Example 4: Push Notification Navigation

```kotlin
// When handling push notifications
private fun handleNotification(messageId: String) {
    val intent = Intent(this, AsaVaultActivity::class.java).apply {
        putExtra("messageId", messageId)
    }
    startActivity(intent)
}
```

#### Example 5: Conditional Navigation

```kotlin
// Based on user authentication or other conditions
private fun openAsaVaultIfAuthenticated() {
    if (isUserAuthenticated()) {
        val intent = Intent(this, AsaVaultActivity::class.java)
        startActivity(intent)
    } else {
        // Show login screen or handle authentication
        showLoginDialog()
    }
}
```

### Integration Patterns

#### Pattern 1: Standalone Feature
Use ASA Vault as a standalone feature accessible from your main menu or navigation.

#### Pattern 2: Contextual Integration
Integrate ASA Vault into specific workflows or user journeys within your app.

#### Pattern 3: Notification-Driven
Use ASA Vault primarily through push notifications and deep links.

### Best Practices

1. **User Experience**: Always provide clear navigation paths to the ASA Vault SDK
2. **Error Handling**: Handle cases where the SDK might not be available
3. **Loading States**: Show appropriate loading indicators while the SDK initializes
4. **Back Navigation**: Ensure proper back navigation from the ASA Vault SDK

### Customization Options

The ASA Vault SDK provides a complete React Native experience within your Android app. Once launched, users will see the full ASA Vault interface with all its features and functionality.

---

*Continue reading for Troubleshooting and other steps...*
