package com.asavault.asavaultsdkcontainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import android.content.Intent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val openAsaVaultSdk= findViewById<MaterialButton>(R.id.open_asa_vault_sdk)
        openAsaVaultSdk.setOnClickListener {
            val intent = Intent(this, AsaVaultActivity::class.java)
            startActivity(intent)
        }

    }
}
