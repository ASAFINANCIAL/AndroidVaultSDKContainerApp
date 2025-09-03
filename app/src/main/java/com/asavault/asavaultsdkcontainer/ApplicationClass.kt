package com.asavault.asavaultsdkcontainer;

import android.app.Application
import com.asavault.nativelibary.ReactNativeHostManagerNew
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.google.firebase.FirebaseApp

 class ApplicationClass : Application(),ReactApplication {
    override fun onCreate() {
        super.onCreate()
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