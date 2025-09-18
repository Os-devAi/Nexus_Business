package com.nexusdev.nexusbusiness.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.nexusbusiness.model.CategoriasModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoriasViewModel : ViewModel() {

    private val _categorias = MutableStateFlow<List<CategoriasModel>>(emptyList())
    val categorias: StateFlow<List<CategoriasModel>> = _categorias

    val db = FirebaseFirestore.getInstance()

    fun fetchCategorias() {
        db.collection("categorias")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        val categorias = doc.toObject(CategoriasModel::class.java)
                        categorias?.copy(id = doc.id)
                    }
                    _categorias.value = list
                }
            }
    }
}