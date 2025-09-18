package com.nexusdev.nexusbusiness.ui.screens.detalles

import android.Manifest
import android.R.attr.rating
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import com.nexusdev.nexusbusiness.model.ReviewModel

@Composable
fun DetallesScreen(
    id: String,
    modifier: Modifier
) {
    // variables de sistema
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    //viewmodel
    val viewModel: NegociosViewModel = viewModel()
    val negocios by viewModel.negocios.collectAsState()

    var userName by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }
    var visibleReviews by remember { mutableStateOf(5) }

    val reviewResult by viewModel.reviewResult.collectAsState()
    val reviews by viewModel.reviews.collectAsState()

    LaunchedEffect(id) {
        viewModel.fetchDetalles(id)
        viewModel.getReviewsById(id)
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
                    Text(
                        String.format("%.1f", negocios[0].calificacion ?: 0.0),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text("(${reviews.size ?: 0})", fontSize = 14.sp)
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

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Formulario de reseña
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Deja tu reseña", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Tu nombre") },
                            singleLine = true
                        )

                        Row {
                            (1..5).forEach { star ->
                                IconButton(onClick = { rating = star }) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star $star",
                                        tint = if (star <= rating) Color(0xFFFFC107) else Color.Gray
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Comentario (opcional)") },
                            maxLines = 3
                        )

                        FilledTonalButton(
                            onClick = {
                                if (userName.isNotBlank() && rating > 0) {
                                    viewModel.addReview(
                                        id = negocios[0].id ?: "",
                                        userName = userName,
                                        rating = rating,
                                        comment = comment
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Por favor, completa todos los campos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                userName = ""
                                rating = 0
                                comment = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text("Enviar Reseña")
                        }

                        reviewResult?.let {
                            Text(it, fontWeight = FontWeight.SemiBold, color = Color.Green)
                        }
                    }
                }

                // Lista de reseñas
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Reseñas", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    if (reviews.isEmpty()) {
                        Text(
                            "Aún no hay reseñas para este taxi",
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        reviews.take(visibleReviews).forEach { review ->
                            ReviewCard(review = review)
                        }

                        if (reviews.size > visibleReviews) {
                            Button(
                                onClick = { visibleReviews += 5 },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text("Mostrar más reseñas")
                            }
                        }
                    }
                }
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
                                Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            context.startActivity(intent)
                        } else {
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.CALL_PHONE),
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


@Composable
fun ReviewCard(review: ReviewModel) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(review.userName ?: "Usuario", fontWeight = FontWeight.SemiBold)

                Row {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (star <= (review.rating
                                    ?: 0)
                            ) Color(0xFFFFC107) else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            review.comment?.let {
                if (it.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, fontSize = 14.sp, lineHeight = 18.sp)
                }
            }
        }
    }
}