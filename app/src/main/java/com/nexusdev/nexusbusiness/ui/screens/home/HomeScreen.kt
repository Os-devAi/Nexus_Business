package com.nexusdev.nexusbusiness.ui.screens.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.nexusdev.nexusbusiness.R
import com.nexusdev.nexusbusiness.components.CategoriasItem
import com.nexusdev.nexusbusiness.components.NegocioItem
import com.nexusdev.nexusbusiness.presentation.CategoriasViewModel
import com.nexusdev.nexusbusiness.presentation.NegociosViewModel

@Composable
fun HomeScreen(modifier: Modifier, nav: NavController) {

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

    // viewModel negociso
    val viewModelNegocios: NegociosViewModel = viewModel()
    val negocios by viewModelNegocios.negocios.collectAsState()

    var category by remember { mutableStateOf("") }


    // obtener las categorías
    LaunchedEffect(Unit) {
        viewModel.fetchCategorias()
    }

    Column(
        modifier = modifier.verticalScroll(scrollState)
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
                CategoriasItem(
                    categoria = categoria,
                    modifier = Modifier.clickable { category = categoria.nombre.toString() })
            }
        }

        // body
        Column {
            LaunchedEffect(category) {
                if (category.isNotEmpty()) {
                    viewModelNegocios.fetchNegociosByCat(category)
                }
            }

            Text(
                "Negocios",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    category,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            negocios.let { lista ->
                if (lista.isNotEmpty()) {
                    lista.forEach { item ->
                        NegocioItem(negocio = item, modifier = Modifier, nav = nav)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Selecciona una categoría",
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize
                        )
                        Image(
                            painter = painterResource(R.drawable.business_ico),
                            contentDescription = "",
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                        )
                    }
                }
            }


            // footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("® Nexus Dev - 2025",
                    style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}