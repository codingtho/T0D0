package com.codingtho.t0d0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codingtho.t0d0.ui.screen.view.MainScreen
import com.codingtho.t0d0.ui.theme.T0D0Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            T0D0Theme {
                MainScreen()
            }
        }
    }
}
