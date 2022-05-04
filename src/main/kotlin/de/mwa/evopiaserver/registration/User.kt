package de.mwa.evopiaserver.db.kotlin.de.mwa.evopiaserver.registration;

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = -1,
    val firstName: String,
    val lastName: String,
    val dateOfRegistration: String,
    val email: String,
    var password: String,
    val imagePath: String
)
