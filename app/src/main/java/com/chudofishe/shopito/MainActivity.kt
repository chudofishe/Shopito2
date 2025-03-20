package com.chudofishe.shopito

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.chudofishe.shopito.auth.FirebaseAuthHandler
import com.chudofishe.shopito.ui.theme.ShopitoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopitoTheme {
                ComposeApp(
                    signInRequest = {
                        lifecycleScope.launch {
                            FirebaseAuthHandler.handleSignIn(this@MainActivity) {
                                Log.d("MainActivity", "uspeh")
                            }
                        }
                    },
                    signOutRequest = {
                        lifecycleScope.launch {
                            FirebaseAuthHandler.handleSignOut(this@MainActivity)
                        }
                    },
                    onExitApp = {
                        finish()
                    }
                )
            }
        }
    }
}

