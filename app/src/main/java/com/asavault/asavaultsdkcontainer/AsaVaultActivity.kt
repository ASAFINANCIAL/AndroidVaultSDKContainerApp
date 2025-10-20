package com.asavault.asavaultsdkcontainer

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
        listener: PermissionListener?   // <-- nullable to match RN’s interface
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
           val sdkParamsBundle = Bundle().apply {
//                KEY_PLACEHOLDER
               putString("Subscriptionkey", "KEY_PLACEHOLDER")
               putInt("AsaFintechCode", AsaFintechCode_PLACEHOLDER)
               putInt("ApplicationCode", ApplicationCode_PLACEHOLDER)
               putString("AuthorizationKey", "AuthorizationKey_PLACEHOLDER")
           }
           putBundle("asavaultsdkparamter", sdkParamsBundle)
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

        // Keep normal AppCompat flow if RN didn’t consume it
        if (!consumed) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
