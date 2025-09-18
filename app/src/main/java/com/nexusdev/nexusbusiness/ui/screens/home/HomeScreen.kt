package com.nexusdev.nexusbusiness.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.nexusdev.nexusbusiness.components.CategoriasItem
import com.nexusdev.nexusbusiness.presentation.CategoriasViewModel

@Composable
fun HomeScreen(modifier: Modifier) {

    // variables de sistema
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // viewModel
    val viewModel: CategoriasViewModel = viewModel()
    val categorias by viewModel.categorias.collectAsState()

    // obtener las categorías
    LaunchedEffect(Unit) {
        viewModel.fetchCategorias()
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {

        //header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (isLandscape) {
                        120.dp
                    } else {
                        120.dp
                    }
                )
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) {
                    Color.White.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Nexus Business",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // categorías
        Text(
            "Categorías",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            categorias.forEach { categoria ->
                CategoriasItem(categoria = categoria)
            }
        }
    }

    // body


    // footer
}