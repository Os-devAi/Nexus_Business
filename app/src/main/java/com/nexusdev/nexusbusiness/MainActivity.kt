package com.nexusdev.nexusbusiness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nexusdev.nexusbusiness.navigation.NavController
import com.nexusdev.nexusbusiness.ui.theme.NexusBusinessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusBusinessTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavController(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}