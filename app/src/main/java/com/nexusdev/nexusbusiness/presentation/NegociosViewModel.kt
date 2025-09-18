package com.nexusdev.nexusbusiness.presentation

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.nexusbusiness.model.NegocioModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NegociosViewModel : ViewModel() {
    private val _negocios = MutableStateFlow<List<NegocioModel>>(emptyList())
    val negocios: StateFlow<List<NegocioModel>> = _negocios

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
}