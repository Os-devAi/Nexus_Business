package com.nexusdev.nexusbusiness.model

data class NegocioModel(
    val id: String? = null,
    val nombre: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null,
    val horario: String? = null,
    val imagenUrl: String? = null,
    val latitud: String? = null,
    val longitud: String? = null,
    val direccion: String? = null,
    val servicios: String? = null,
    val telefono: String? = null,
    val telefonoSegundo: String? = null,
    val calificacion: Double? = 0.0
)
