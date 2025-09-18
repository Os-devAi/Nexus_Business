package com.nexusdev.nexusbusiness.presentation

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.nexusbusiness.model.NegocioModel
import com.nexusdev.nexusbusiness.model.ReviewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NegociosViewModel : ViewModel() {
    private val _negocios = MutableStateFlow<List<NegocioModel>>(emptyList())
    val negocios: StateFlow<List<NegocioModel>> = _negocios

    private val _reviews = MutableStateFlow<List<ReviewModel>>(emptyList())
    val reviews: StateFlow<List<ReviewModel>> = _reviews

    private val _reviewResult = MutableStateFlow<String?>(null)
    val reviewResult: StateFlow<String?> = _reviewResult

    val db = FirebaseFirestore.getInstance()

    fun fetchNegociosByCat(categoria: String) {
        db.collection("negocios")
            .whereEqualTo("categoria", categoria)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val negociosList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(NegocioModel::class.java)?.copy(id = doc.id)
                    }
                    _negocios.value = negociosList
                }
            }
    }

    fun fetchDetalles(id: String) {
        db.collection("negocios")
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val negocio = documentSnapshot.toObject(NegocioModel::class.java)
                _negocios.value = listOfNotNull(negocio)

            }
    }


    // Lista ampliada de palabras ofensivas
    private val badWords = listOf(
        "tonto", "estupido", "idiota", "pendejo", "puta", "mierda", "verga",
        "mal parido", "hijo de puta", "madre", "putos", "puto", "gilipollas", "imbécil",
        "cabron", "coño", "culero", "mierdoso", "zorra", "maricón", "joto", "culia(o|s)",
        "pajero", "chingar", "chingada", "chingado", "pinche", "cabrón", "cojones", "puta madre",
        "hijo de la chingada", "mamón", "mamona", "pendeja", "pendejo", "culia", "culiao",
        "zopenco", "burro", "baboso", "tarado", "tonta", "imbécil", "cagada", "cagar",
        "pico", "cuca", "vagina", "pene"
    )

    // Validar que un comentario no tenga palabras ofensivas
    private fun isCommentValid(text: String): Boolean {
        val normalized = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .replace("[^a-z0-9\\s]".toRegex(), "")

        badWords.forEach { badWord ->
            val regex = Regex("\\b${Regex.escape(badWord)}\\b", RegexOption.IGNORE_CASE)
            if (regex.containsMatchIn(normalized)) return false
        }
        return true
    }

    // Agregar reseña (solo si es válida)
    fun addReview(id: String, userName: String, rating: Int, comment: String) {
        if (!isCommentValid(comment)) {
            _reviewResult.value = "No se permiten comentarios con palabras ofensivas ❌"
            return
        }

        val review = hashMapOf(
            "userName" to userName,
            "rating" to rating,
            "comment" to comment,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("negocios")
            .document(id)
            .collection("reviews")
            .add(review)
            .addOnSuccessListener {
                _reviewResult.value = "Reseña enviada ✅"
                getReviewsById(id)
                updateTaxiRating(id)
            }
            .addOnFailureListener {
                _reviewResult.value = "Error al enviar reseña ❌"
            }
    }

    // Actualizar calificación promedio del taxi
    private fun updateTaxiRating(taxiId: String) {
        db.collection("negocios")
            .document(taxiId)
            .collection("reviews")
            .get()
            .addOnSuccessListener { snapshot ->
                val ratings = snapshot.documents.mapNotNull { it.getLong("rating")?.toInt() }
                if (ratings.isNotEmpty()) {
                    val avg = ratings.average()
                    db.collection("negocios")
                        .document(taxiId)
                        .update("calificacion", avg)
                }
            }
    }

    // Obtener reseñas por taxi
    fun getReviewsById(id: String) {
        db.collection("negocios").document(id).collection("reviews")
            .addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ReviewModel::class.java)
                    }
                    _reviews.value = list
                }
            }
    }
}