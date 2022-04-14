package de.mwa.evopiaserver.registration;

data class User(
    val id: Long = -1,
    val firstName: String,
    val lastName: String,
    val dateOfRegistration: String,
    val email: String,
    var password: String,
    val imagePath: String
)
