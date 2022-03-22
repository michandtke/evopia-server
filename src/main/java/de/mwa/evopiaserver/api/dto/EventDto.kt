package de.mwa.evopiaserver.api.dto

data class EventDto(
    val id: Int?,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val place: String,
    val imagePath: String
)
