package com.shopease.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shopease.app.ui.navigation.ShopEaseScaffold
import com.shopease.app.ui.theme.ShopEaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopEaseApp()
        }
    }
}

@Composable
fun ShopEaseApp() {
    ShopEaseTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ShopEaseScaffold()
        }
    }
}
