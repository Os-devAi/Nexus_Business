package com.nexusdev.nexusbusiness.model

data class ReviewModel(
    val id: String? = null,
    val userName: String? = null,
    val rating: Int? = null,
    val comment: String? = null,
    val timestamp: Long? = null
) {
    // Firestore necesita un constructor vacío
    constructor() : this(null, null, null, null, null)
}