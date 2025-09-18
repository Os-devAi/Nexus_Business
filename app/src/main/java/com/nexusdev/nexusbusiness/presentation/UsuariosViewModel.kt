package com.nexusdev.nexusbusiness.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexusdev.nexusbusiness.model.UsuarioModel

class UsuariosViewModel {
    private val _usuarios = MutableLiveData<List<UsuarioModel>>()
    val usuarios: LiveData<List<UsuarioModel>> = _usuarios
}