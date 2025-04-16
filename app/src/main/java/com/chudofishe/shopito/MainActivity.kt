package com.chudofishe.shopito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chudofishe.shopito.ui.ComposeRoot
import com.chudofishe.shopito.ui.theme.ShopitoTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopitoTheme {
                ComposeRoot()
            }
        }
    }
}

