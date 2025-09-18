package com.nexusdev.nexusbusiness.ui.screens.detalles

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.nexusdev.nexusbusiness.presentation.NegociosViewModel
import androidx.core.net.toUri

@Composable
fun DetallesScreen(
    id: String,
    modifier: Modifier
) {
    // variables de sistema
    val scrollState = rememberScrollState()
    //viewmodel
    val viewModel: NegociosViewModel = viewModel()
    val negocios by viewModel.negocios.collectAsState()

    LaunchedEffect(id) {
        viewModel.fetchDetalles(id)
    }

    if (negocios.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = negocios[0].imagenUrl,
                contentDescription = "Imagen del taxi",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    negocios[0].nombre ?: "N/A",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Calificación destacada
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFFFFC107)
                    )
//                    Text(
//                        "${String.format("%.1f", taxi.calificacion ?: 0.0)}",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Medium
//                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text("INFORMACIÓN DEL NEGOCIO", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoItem("Dirección", negocios[0].direccion ?: "N/A")
                        InfoItem("Teléfono", negocios[0].telefono ?: "N/A")
                        InfoItem("Horario", negocios[0].horario ?: "N/A")
                        InfoItem("Servicios", negocios[0].servicios ?: "N/A")
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Información de contacto
                Text("CONTACTO", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

                DriverSection(
                    name = negocios[0].nombre ?: "N/A",
                    phone = negocios[0].telefono,
                    context = LocalContext.current
                )
            }
        }

    } else {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando detalles...", fontSize = 16.sp)
        }

    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@SuppressLint("UseKtx")
@Composable
fun DriverSection(name: String, phone: String?, context: Context) {
    val activity = context as Activity
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(name, fontWeight = FontWeight.SemiBold)

        phone?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        val phoneNumber = "tel:$phone"
                        val intent = Intent(Intent.ACTION_CALL, phoneNumber.toUri())

                        if (ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            context.startActivity(intent)
                        } else {
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(android.Manifest.permission.CALL_PHONE),
                                100
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Llamar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Llamar")
                }

                FilledTonalButton(
                    onClick = {
                        val whatsappNumber = phone.replace(" ", "").replace("-", "")
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = "https://wa.me/502$whatsappNumber".toUri()
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.MailOutline, contentDescription = "WhatsApp")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp")
                }
            }
        }
    }
}
